package com.github.thomasfischl.eurydome.backend.model;

import com.mongodb.BasicDBObject;

public class DOUser extends AbstractDomainObject {

  public DOUser() {
    super();
  }

  public DOUser(BasicDBObject object) {
    super(object);
  }

  public void setOrganisationRef(String organisation) {
    object.put("organisation", organisation);
  }

  public String getOrganisationRef() {
    return object.getString("organisation");
  }
}