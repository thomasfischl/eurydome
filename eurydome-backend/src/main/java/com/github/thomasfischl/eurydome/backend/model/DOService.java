package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOService extends AbstractDomainObject {

  public final static String STOPPED = "Stopped";
  public final static String STARTING = "Starting";
  public final static String STARTED = "Started";

  public DOService() {
    super();
  }

  public DOService(BasicDBObject object) {
    super(object);
  }

  public void setName(String name) {
    object.put("name", name);
  }

  public String getName() {
    return object.getString("name");
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
  }

  public String getStatus() {
    return object.getString("status");
  }
}
