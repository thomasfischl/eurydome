package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DODockerHost extends AbstractDomainObject {

  public DODockerHost() {
    super();
  }

  public DODockerHost(BasicDBObject object) {
    super(object);
  }

  public void setRemoteApiUrl(String remoteApiUrl) {
    object.put("remoteApiUrl", remoteApiUrl);
  }

  public String getRemoteApiUrl() {
    return object.getString("remoteApiUrl");
  }

  public void setCertificateArchive(String certificateArchive) {
    object.put("certificateArchive", certificateArchive);
  }
  
  public String getCertificateArchive() {
    return object.getString("certificateArchive");
  }

  public void setContainerUrl(String containerUrl) {
    object.put("containerUrl", containerUrl);
  }
  
  public String getContainerUrl() {
    return object.getString("containerUrl");
  }
  
}
