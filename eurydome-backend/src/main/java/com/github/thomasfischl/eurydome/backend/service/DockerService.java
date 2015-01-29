package com.github.thomasfischl.eurydome.backend.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
import com.github.thomasfischl.eurydome.backend.dal.FileDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.dal.SettingDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.model.DOSetting;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;
import com.github.thomasfischl.eurydome.backend.util.ZipUtil;

@Service
public class DockerService {

  @Inject
  private SettingDataStore settingStore;

  @Inject
  private ApplicationDataStore applicationStore;

  @Inject
  private ServiceDataStore serviceStore;

  @Inject
  private FileDataStore fileStore;

  @Inject
  private ProxyService proxyService;

  private Queue<DockerTask> backlog = new ConcurrentLinkedQueue<DockerTask>();

  public void startService(DOService service) {
    DOApplication application = applicationStore.findById(service.getApplicationRef());
    if (application == null) {
      throw new IllegalArgumentException("No applicaton with id '" + service.getApplicationRef() + "' found.");
    }

    DOFile file = fileStore.findById(application.getDockerArchive());
    if (file == null) {
      throw new IllegalArgumentException("No file with id '" + application.getDockerArchive() + "' found.");
    }

    DockerTask task = new DockerTask(service, application, file);
    backlog.add(task);

    //
    // update service
    //
    service.setStatus(DOService.STARTING);
    serviceStore.save(service);
  }

  public void stopService(DOService service) {
    DockerClient client = getClient();

    //
    // stop container
    //
    Container oldContainer = DockerUtil.getContainerByName(client, getContainerName(service));
    if (oldContainer != null) {
      DockerUtil.deleteContainer(client, oldContainer);
    }

    //
    // update service
    //
    service.setStatus(DOService.STOPPED);
    service.setExposedPort(null);
    serviceStore.save(service);
  }

  @Scheduled(fixedDelay = 5000)
  public void execute() {
    DockerTask task = backlog.poll();
    if (task == null) {
      return;
    }

    try {
      execute(task);
    } catch (Exception e) {
      task.getService().setStatus(DOService.FAILED + "(" + e.getMessage() + ")");
      task.getService().setExposedPort(null);
      serviceStore.save(task.getService());
      e.printStackTrace();
    }
  }

  public void execute(DockerTask task) throws IOException {
    String containerName = getContainerName(task.getService());
    DockerClient client = getClient();

    //
    // remove old container
    //
    Container oldContainer = DockerUtil.getContainerByName(client, containerName);
    if (oldContainer != null) {
      DockerUtil.deleteContainer(client, oldContainer);
      oldContainer = DockerUtil.getContainerByName(client, containerName);
      if (oldContainer != null) {
        throw new IllegalStateException("The container '" + containerName + "' already extis.");
      }
    }

    //
    // upload docker archive and build image
    //
    InputStream response = client.buildImageCmd(fileStore.getInputStream(task.getFile().getId()))
        .withTag(containerName).withNoCache(false).exec();
    StringWriter logwriter = new StringWriter();

    try {
      LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
      while (itr.hasNext()) {
        String line = itr.next();
        logwriter.write(line);
        System.out.println("Line: " + line);
      }
    } finally {
      IOUtils.closeQuietly(response);
    }

    Image image = DockerUtil.getImage(client, containerName + ":latest");
    if (image == null) {
      throw new IllegalStateException("No image with tag '" + containerName + "' found.");
    }

    //
    // create container from image
    //
    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName(containerName);
    CreateContainerResponse containerResp = container.exec();

    String[] warnings = containerResp.getWarnings();
    if (warnings != null) {
      for (String warning : warnings) {
        System.out.println("Warning: " + warning);
      }
    }

    //
    // start container
    //
    StartContainerCmd startContainer = client.startContainerCmd(containerResp.getId());
    startContainer.withPublishAllPorts(true);
    startContainer.exec();

    InspectContainerResponse continerInfo = client.inspectContainerCmd(containerResp.getId()).exec();

    NetworkSettings networkSettings = continerInfo.getNetworkSettings();
    String port = null;
    for (Entry<ExposedPort, Binding[]> entry : networkSettings.getPorts().getBindings().entrySet()) {
      Binding binding = entry.getValue()[0];
      port = String.valueOf(binding.getHostPort());
    }

    client.close();

    //
    // update service
    //
    task.getService().setStatus(DOService.STARTED);
    task.getService().setExposedPort(port);
    serviceStore.save(task.getService());

    //
    // update proxy
    //
    proxyService.updateConfiguration(getDockerConfiguration(), serviceStore.findAll());
    proxyService.reloadProxy();
  }

  private DockerClient getClient() {
    DockerHostConfiguration config = getDockerConfiguration();

    //
    // prepare docker host certificates
    //
    DOFile file = fileStore.findById(config.getCertificateRef());
    if (file == null) {
      throw new IllegalStateException("No docker certificates available.");
    }
    File tempDir = new File(FileUtils.getTempDirectory(), "eurydome");
    ZipUtil.extract(tempDir, fileStore.getInputStream(config.getCertificateRef()));

    //
    // create docker client
    //
    String url = "https://" + config.getHost() + ":2376";
    return DockerUtil.createClient(url, tempDir.getAbsolutePath());
  }

  private String getContainerName(DOService service) {
    return service.getName().replaceAll(" ", "_").toLowerCase();
  }

  private DockerHostConfiguration getDockerConfiguration() {
    String host = settingStore.findByName(DOSetting.SETTING_DOCKER_HOST).getValue();
    String certificatesRef = settingStore.findByName(DOSetting.SETTING_DOCKER_CERTS).getValue();

    if (host == null) {
      throw new IllegalArgumentException("Invalid docker configuration. Host: null");
    }
    if (certificatesRef == null) {
      throw new IllegalArgumentException("Invalid docker configuration. Certificates: null");
    }

    DockerHostConfiguration config = new DockerHostConfiguration(host, certificatesRef);
    return config;
  }

}

class DockerTask {
  private final DOService service;
  private final DOApplication application;
  private final DOFile file;

  public DockerTask(DOService service, DOApplication application, DOFile file) {
    this.service = service;
    this.application = application;
    this.file = file;
  }

  public DOApplication getApplication() {
    return application;
  }

  public DOService getService() {
    return service;
  }

  public DOFile getFile() {
    return file;
  }
}

class DockerHostConfiguration {
  private final String host;
  private final String certificationRef;

  public DockerHostConfiguration(String host, String certificationRef) {
    this.host = host;
    this.certificationRef = certificationRef;
  }

  public String getHost() {
    return host;
  }

  public String getCertificateRef() {
    return certificationRef;
  }
}