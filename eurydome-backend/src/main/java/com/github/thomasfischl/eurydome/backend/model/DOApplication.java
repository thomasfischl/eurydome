package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOApplication extends AbstractDomainObject {

  public DOApplication() {
    super();
  }

  public DOApplication(BasicDBObject object) {
    super(object);
  }

  public String getName() {
    return object.getString("name");
  }

  public void setName(String name) {
    object.put("name", name);
  }

  public String getLocation() {
    return object.getString("location");
  }

  public void setLocation(String location) {
    object.put("location", location);
  }

  @Override
  public void validate() {
    super.validate();

    if (getName() == null) {
      setName("DOApplication " + getId());
    }
  }

}
