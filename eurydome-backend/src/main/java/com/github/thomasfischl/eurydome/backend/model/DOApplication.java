package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOApplication extends AbstractDomainObject {

  public DOApplication() {
    super();
  }

  public DOApplication(BasicDBObject object) {
    super(object);
  }

  public String getDockerArchive() {
    return object.getString("dockerArchive");
  }

  public void setDockerArchive(String dockerArchive) {
    object.put("dockerArchive", dockerArchive);
  }

  public String getProxyConfig() {
    return object.getString("proxyConfig");
  }

  public void setProxyConfig(String proxyConfig) {
    object.put("proxyConfig", proxyConfig);
  }

  public String getHealthCheckUrl() {
    return object.getString("healthCheckUrl");
  }
  
  public void setHealthCheckUrl(String healthCheckUrl) {
    object.put("healthCheckUrl", healthCheckUrl);
  }

  public String getBaseServiceUrl() {
    return object.getString("baseServiceUrl");
  }
  
  public void setBaseServiceUrl(String baseServiceUrl) {
    object.put("baseServiceUrl", baseServiceUrl);
  }

}
