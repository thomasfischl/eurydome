package com.github.thomasfischl.eurydome.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;

public class AbstractDomainObject {

  protected BasicDBObject object;

  public AbstractDomainObject() {
    object = new BasicDBObject();
  }

  @JsonIgnore
  public BasicDBObject getDataObject() {
    return object;
  }

  public AbstractDomainObject(BasicDBObject object) {
    this.object = object;
  }

  public String getId() {
    return object.getString("id");
  }

  public void setId(String id) {
    object.put("id", id);
  }

  public void setName(String name) {
    object.put("name", name);
  }

  public String getName() {
    return object.getString("name");
  }

  public void validate() {
  }

  @Override
  public String toString() {
    if (getName() != null) {
      return getName() + "(" + getId() + ")";
    } else {
      return getId();
    }
  }

}
