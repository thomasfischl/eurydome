package com.github.thomasfischl.eurydome.backend.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.thomasfischl.eurydome.backend.dal.DockerHostDataStore;
import com.github.thomasfischl.eurydome.backend.dal.FileDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.service.ProxyService;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;

@Component("TaskStopContainer")
public class TaskStopContainer extends AbstractTask {

  public static final String DOCKER_HOST_ID = "TaskStopContainer_DockerHostId";

  public static final String SERVICE_ID = "TaskStopContainer_ServiceId";

  @Inject
  private ServiceDataStore serviceStore;

  @Inject
  private FileDataStore fileStore;

  @Inject
  private DockerHostDataStore dockerhostStore;

  @Inject
  private ProxyService proxyService;

  private DOService service;

  private DODockerHost dockerHost;

  private DockerClient client;

  @Override
  public List<StepDefinition> getSteps() {
    List<StepDefinition> steps = new ArrayList<StepDefinition>();
    steps.add(new StepDefinition("Prepare", () -> stepPrepare()));
    steps.add(new StepDefinition("Initialize Docker Client", () -> stepCreateDockerClient()));
    steps.add(new StepDefinition("Stop Container", () -> stepStopContainer()));
    steps.add(new StepDefinition("Update Service State", () -> stepUpdateService()));
    steps.add(new StepDefinition("Update Proxy Configuration", () -> stepUpdateProxyConfiguration()));
    return steps;
  }

  @Override
  public void complete() {
    super.complete();
    service.setStatus(DOService.STOPPED);
    service.setErrorMessage(null);
    service.setExposedPort(null);
    service.setActualDockerHost(null);
    serviceStore.save(service);
  }

  @Override
  public void completeWithError(String errorMsg) {
    super.completeWithError(errorMsg);
    service.setStatus(DOService.FAILED);
    service.setErrorMessage(errorMsg);
    service.setExposedPort(null);
    service.setActualDockerHost(null);
    serviceStore.save(service);
  }

  public static String getTaskName() {
    return "TaskStopContainer";
  }

  private void stepPrepare() {
    service = serviceStore.findById(getSetting(SERVICE_ID));
    dockerHost = dockerhostStore.findById(getSetting(DOCKER_HOST_ID));

    if (service.getName() == null) {
      throw new IllegalStateException("No service name defined.");
    }
  }

  private void stepCreateDockerClient() {
    client = DockerUtil.createClient(dockerHost, fileStore);
  }

  private void stepStopContainer() {
    logMessage("Stop Container '" + service.getName() + "'");
    if (service.getName() != null) {
      Container oldContainer = DockerUtil.getContainerByName(client, DockerUtil.normalizeContainerName(service));
      if (oldContainer != null) {
        DockerUtil.deleteContainer(client, oldContainer);
      }
    }
  }

  private void stepUpdateService() {
    logMessage("Update service state");
    service.setStatus(DOService.STOPPED);
    service.setExposedPort(null);
    service.setActualDockerHost(null);
    serviceStore.save(service);
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
}
