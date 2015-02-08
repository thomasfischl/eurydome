package com.github.thomasfischl.eurydome.backend.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.dal.ApplicationDataStore;
import com.github.thomasfischl.eurydome.backend.dal.DockerHostDataStore;
import com.github.thomasfischl.eurydome.backend.dal.FileDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.dal.TaskDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.model.DOTask;
import com.github.thomasfischl.eurydome.backend.task.TaskStartContainer;
import com.github.thomasfischl.eurydome.backend.task.TaskStopContainer;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;

@Service
public class DockerService {

  private final static Log LOG = LogFactory.getLog(DockerService.class);

  @Inject
  private ApplicationDataStore applicationStore;

  @Inject
  private ServiceDataStore serviceStore;

  @Inject
  private FileDataStore fileStore;

  @Inject
  private TaskDataStore taskStore;

  @Inject
  private DockerHostDataStore dockerhostStore;

  public void startService(DOService service) {
    DOApplication application = applicationStore.findById(service.getApplicationRef());
    if (application == null) {
      throw new IllegalArgumentException("No applicaton with id '" + service.getApplicationRef() + "' found.");
    }

    DOFile file = fileStore.findById(application.getDockerArchive());
    if (file == null) {
      throw new IllegalArgumentException("No file with id '" + application.getDockerArchive() + "' found.");
    }

    DODockerHost dockerHost = null;
    if (service.getPreferDockerHost() != null) {
      dockerHost = dockerhostStore.findById(service.getPreferDockerHost());
      if (dockerHost == null) {
        throw new IllegalStateException("The prefered docker host '" + service.getPreferDockerHost()
            + "' can't be found.");
      }
    } else {
      List<DODockerHost> dockerHosts = dockerhostStore.findAll();
      for (DODockerHost host : dockerHosts) {
        try {
          testConnection(host);
          dockerHost = host;
        } catch (Exception e) {
          LOG.debug("Find active docker host.", e);
        }
      }
      if (dockerHost == null) {
        throw new IllegalStateException("No active docker host found.");
      }
    }

    //
    // create task
    //
    DOTask task = taskStore.createObject();
    task.setName("Start Service '" + service.getName() + "'");
    task.setTaskType(TaskStartContainer.getTaskName());
    task.setSetting(TaskStartContainer.APPLICATION_ID, application.getId());
    task.setSetting(TaskStartContainer.DOCKER_HOST_ID, dockerHost.getId());
    task.setSetting(TaskStartContainer.FILE_ID, file.getId());
    task.setSetting(TaskStartContainer.SERVICE_ID, service.getId());
    taskStore.save(task);

    //
    // update service
    //
    service.setStatus(DOService.MAINTENANCE);
    service.setActualDockerHost(dockerHost.getId());
    service.setCurrentTask(task.getId());
    serviceStore.save(service);
  }

  public void stopService(DOService service) {
    DODockerHost dockerHost = dockerhostStore.findById(service.getActualDockerHost());
    if (dockerHost == null) {
      service.setStatus(DOService.STOPPED);
      service.setExposedPort(null);
      service.setActualDockerHost(null);
      service.setErrorMessage("No docker host with id '" + service.getActualDockerHost() + "' found.");
      serviceStore.save(service);

      throw new IllegalStateException("No docker host with id '" + service.getActualDockerHost() + "' found.");
    }

    //
    // create task
    //
    DOTask task = taskStore.createObject();
    task.setName("Stop Service '" + service.getName() + "'");
    task.setTaskType(TaskStopContainer.getTaskName());
    task.setSetting(TaskStopContainer.DOCKER_HOST_ID, dockerHost.getId());
    task.setSetting(TaskStopContainer.SERVICE_ID, service.getId());
    taskStore.save(task);

    //
    // update service
    //
    service.setStatus(DOService.MAINTENANCE);
    service.setCurrentTask(task.getId());
    serviceStore.save(service);
  }

  public void testConnection(DODockerHost dockerhost) {
    DockerUtil.testConnection(DockerUtil.createClient(dockerhost, fileStore));
  }
}