package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOService extends AbstractDomainObject {

  public final static String STOPPED = "Stopped";
  public final static String MAINTENANCE = "Maintenance";
  public final static String STARTED = "Started";
  public final static String FAILED = "Failed";

  public DOService() {
    super();
  }

  public DOService(BasicDBObject object) {
    super(object);
  }

  public void setUrl(String url) {
    object.put("url", url);
  }

  public String getUrl() {
    return object.getString("url");
  }

  public void setApplicationRef(String application) {
    object.put("application-ref", application);
  }

  public String getApplicationRef() {
    return object.getString("application-ref");
  }

  public void setExposedPort(String application) {
    object.put("exposedPort", application);
  }

  public String getExposedPort() {
    return object.getString("exposedPort");
  }

  public void setStatus(String status) {
    object.put("status", status);
    if (!FAILED.equals(status)) {
      setErrorMessage(null);
    }
  }

  public String getStatus() {
    return object.getString("status");
  }

  public void setErrorMessage(String errorMessage) {
    object.put("errorMessage", errorMessage);
  }

  public String getErrorMessage() {
    return object.getString("errorMessage");
  }

  public void setContainerId(String containerId) {
    object.put("containerId", containerId);
  }

  public String getContainerId() {
    return object.getString("containerId");
  }

  public void setPreferDockerHost(String preferDockerHost) {
    object.put("preferDockerHost", preferDockerHost);
  }

  public String getPreferDockerHost() {
    return object.getString("preferDockerHost");
  }

  public void setActualDockerHost(String actualDockerHost) {
    object.put("actualDockerHost", actualDockerHost);
  }

  public String getActualDockerHost() {
    return object.getString("actualDockerHost");
  }

  public void setCurrentTask(String currentTask) {
    object.put("currentTask", currentTask);
  }

  public String getCurrentTask() {
    return object.getString("currentTask");
  }

  public void setDockerEnvSettings(String currentTask) {
    object.put("envSettings", currentTask);
  }
  
  public String getDockerEnvSettings() {
    return object.getString("envSettings");
  }
}
