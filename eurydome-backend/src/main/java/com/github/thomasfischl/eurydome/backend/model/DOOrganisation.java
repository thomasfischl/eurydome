package com.github.thomasfischl.eurydome.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class DOOrganisation extends AbstractDomainObject {

  public DOOrganisation() {
    super();
  }

  public DOOrganisation(BasicDBObject object) {
    super(object);
  }

  @SuppressWarnings("unchecked")
  public List<String> getServices() {
    Object obj = object.get("services");
    if (obj instanceof List) {
      return (List<String>) obj;
    }
    return new ArrayList<String>();
  }

  public void setServices(List<String> services) {
    object.put("services", services);
  }

}
