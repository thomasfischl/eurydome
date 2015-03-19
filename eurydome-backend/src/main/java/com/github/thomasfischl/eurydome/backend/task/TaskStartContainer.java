package com.github.thomasfischl.eurydome.backend.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse.NetworkSettings;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.thomasfischl.eurydome.backend.dal.ApplicationDataStore;
import com.github.thomasfischl.eurydome.backend.dal.DockerHostDataStore;
import com.github.thomasfischl.eurydome.backend.dal.FileDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.service.ProxyService;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;
import com.google.common.base.Strings;

@Component("TaskStartContainer")
public class TaskStartContainer extends AbstractTask {

  private final static Log LOG = LogFactory.getLog(TaskStartContainer.class);

  public static final String DOCKER_HOST_ID = "TaskStartContainer_DockerHostId";

  public static final String FILE_ID = "TaskStartContainer_FileId";

  public static final String APPLICATION_ID = "TaskStartContainer_ApplicationId";

  public static final String SERVICE_ID = "TaskStartContainer_ServiceId";

  @Inject
  private ApplicationDataStore applicationStore;

  @Inject
  private ServiceDataStore serviceStore;

  @Inject
  private FileDataStore fileStore;

  @Inject
  private DockerHostDataStore dockerhostStore;

  @Inject
  private ProxyService proxyService;

  private DOService service;

  private DOApplication application;

  private DOFile file;

  private DODockerHost dockerHost;

  private String containerName;

  private String dockerEnvSettings;

  private DockerClient client;

  private Image image;

  private CreateContainerResponse containerResp;

  private String exposedPort;

  @Override
  public List<StepDefinition> getSteps() {
    List<StepDefinition> steps = new ArrayList<StepDefinition>();
    steps.add(new StepDefinition("Prepare", () -> stepPrepare()));
    steps.add(new StepDefinition("Initialize Docker Client", () -> stepCreateDockerClient()));
    steps.add(new StepDefinition("Remove old Container", () -> stepRemoveOldContainer()));
    steps.add(new StepDefinition("Build Container Image", () -> stepBuildContainerImage()));
    steps.add(new StepDefinition("Create Container Instance", () -> stepCreateContainerInstance()));
    steps.add(new StepDefinition("Start Container Instance", () -> stepStartContainerInstance()));
    steps.add(new StepDefinition("Update Service State", () -> stepUpdateService()));
    steps.add(new StepDefinition("Test Service Health Page", () -> stepTestServiceHealthPage()));
    steps.add(new StepDefinition("Update Proxy Configuration", () -> stepUpdateProxyConfiguration()));
    return steps;
  }

  @Override
  public void complete() {
    super.complete();
    service.setStatus(DOService.STARTED);
    service.setErrorMessage(null);
    serviceStore.save(service);
  }

  @Override
  public void completeWithError(String errorMsg) {
    super.completeWithError(errorMsg);
    service.setStatus(DOService.FAILED);
    service.setErrorMessage(errorMsg);
    serviceStore.save(service);
  }

  public static String getTaskName() {
    return "TaskStartContainer";
  }

  private void stepPrepare() {
    service = serviceStore.findById(getSetting(SERVICE_ID));
    application = applicationStore.findById(getSetting(APPLICATION_ID));
    file = fileStore.findById(getSetting(FILE_ID));
    dockerHost = dockerhostStore.findById(getSetting(DOCKER_HOST_ID));

    if (service.getName() == null) {
      throw new IllegalStateException("No service name defined.");
    }
    containerName = DockerUtil.normalizeContainerName(service);
    dockerEnvSettings = service.getDockerEnvSettings();
  }

  private void stepCreateDockerClient() {
    client = DockerUtil.createClient(dockerHost, fileStore);
  }

  private void stepRemoveOldContainer() {
    logMessage("Remove old container");
    Container oldContainer = DockerUtil.getContainerByName(client, containerName);
    if (oldContainer != null) {
      DockerUtil.deleteContainer(client, oldContainer);
      oldContainer = DockerUtil.getContainerByName(client, containerName);
      if (oldContainer != null) {
        throw new IllegalStateException("The container '" + containerName + "' already extis.");
      }
    }
  }

  private void stepBuildContainerImage() {
    logMessage("Upload docker archive '" + file.getName() + "'");

    File tempDockerArchive;
    try {
      // copy to a temporary file do avoid a heap space exception
      tempDockerArchive = Files.createTempFile("docker-archive", "tar").toFile();
      fileStore.writeToFile(file.getId(), tempDockerArchive);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    InputStream response = null;

    try (FileInputStream is = new FileInputStream(tempDockerArchive)) {
      response = client.buildImageCmd(is).withTag(containerName).withNoCache(false).exec();

      LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
      while (itr.hasNext()) {
        String line = transformDockerMessage(itr.next());
        LOG.debug("Line: " + line);
        logMessage(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(response);
    }

    image = DockerUtil.getImage(client, containerName + ":latest");
    if (image == null) {
      throw new IllegalStateException("No image with tag '" + containerName + "' found.");
    }
  }

  private void stepCreateContainerInstance() {
    logMessage("Create container from image '" + image.getId() + "'");
    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName(containerName);
    if (!Strings.isNullOrEmpty(dockerEnvSettings)) {
      container.withEnv(dockerEnvSettings.split(";"));
    }
    containerResp = container.exec();

    String[] warnings = containerResp.getWarnings();
    if (warnings != null) {
      for (String warning : warnings) {
        logMessage("Create Container Warning: " + warning);
      }
    }
  }

  private void stepStartContainerInstance() {
    logMessage("Start container '" + containerResp.getId() + "'");
    StartContainerCmd startContainer = client.startContainerCmd(containerResp.getId());
    startContainer.withPublishAllPorts(true);
    startContainer.exec();

    InspectContainerResponse continerInfo = client.inspectContainerCmd(containerResp.getId()).exec();

    NetworkSettings networkSettings = continerInfo.getNetworkSettings();
    exposedPort = null;
    for (Entry<ExposedPort, Binding[]> entry : networkSettings.getPorts().getBindings().entrySet()) {
      Binding binding = entry.getValue()[0];
      exposedPort = String.valueOf(binding.getHostPort());
    }

    try {
      client.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void stepUpdateService() {
    logMessage("Update service state");
    service.setStatus(DOService.STARTED);
    service.setExposedPort(exposedPort);
    service.setContainerId(containerResp.getId());
    service.setActualDockerHost(dockerHost.getId());
    serviceStore.save(service);
  }

  private void stepTestServiceHealthPage() {
    logMessage("Test health check url");

    String url = dockerHost.getContainerUrl() + ":" + service.getExposedPort();
    if (StringUtils.isNotEmpty(application.getHealthCheckUrl())) {
      url += application.getHealthCheckUrl();
    }

    int successCount = 0;

    for (int idx = 1; idx <= 60; idx++) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Nothing to do
      }

      try {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(1000);
        connection.connect();

        if (connection.getResponseCode() < 400) {
          logMessage("Try " + idx + ": Test health check url '" + url + "'. OK");
          successCount++;
        } else {
          logMessage("Try " + idx + ": Test health check url '" + url + "'. Failed: " + connection.getResponseCode());
          successCount = 0;
        }

        if (successCount == 5) {
          return;
        }
      } catch (Exception e) {
        LOG.info("Unkown resource. " + e.getMessage());
        logMessage("Try " + idx + ": Test health check url '" + url + "'. Failed: " + e.getMessage());
        successCount = 0;
      }
    }

    throw new IllegalStateException("Service '" + service.getName() + "' is not available.");
  }

  private void stepUpdateProxyConfiguration() {
    try {
      logMessage("Update Proxy configuration");
      proxyService.updateConfiguration();
      proxyService.reloadProxy();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String transformDockerMessage(String line) {
    if (line.startsWith("{\"stream\":\" ---\\u003e")) {
      line = "--- " + line.substring(22);
    }
    if (line.startsWith("{\"stream\":\"")) {
      line = line.substring(11);
    }

    if (line.endsWith("\\n\"}")) {
      line = line.substring(0, line.length() - 4);
    }
    return line;
  }

}
