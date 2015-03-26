package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOSetting extends AbstractDomainObject {

  public static final String LOAD_BALANCER_URL = "LOADBALANCER.URL";
  
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
