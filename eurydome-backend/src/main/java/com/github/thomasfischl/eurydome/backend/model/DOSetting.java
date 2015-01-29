package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOSetting extends AbstractDomainObject {

  public final static String SETTING_DOCKER_HOST = "DOCKER.HOST";
  public final static String SETTING_DOCKER_CERTS = "DOCKER.CERTS";

  public DOSetting() {
    super();
  }

  public DOSetting(BasicDBObject object) {
    super(object);
  }

  public void setValue(String value) {
    object.put("value", value);
  }

  public String getValue() {
    return object.getString("value");
  }
}
