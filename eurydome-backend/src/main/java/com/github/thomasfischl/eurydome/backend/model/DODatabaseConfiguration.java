package com.github.thomasfischl.eurydome.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DODatabaseConfiguration {

  private String host;

  private String port;

  public DODatabaseConfiguration() {
  }

  public DODatabaseConfiguration(String host, String port) {
    super();
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  @JsonIgnore
  public int getPortAsInt() {
    return Integer.parseInt(port);
  }

}
