package com.github.thomasfischl.eurydome.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class DOServiceLog extends AbstractDomainObject {

  public DOServiceLog() {
    super();
  }

  public DOServiceLog(BasicDBObject object) {
    super(object);
  }

  @Override
  public void validate() {
    super.validate();
    if (getLogs() == null) {
      setLogs(new ArrayList<String>());
    }
  }

  @SuppressWarnings("unchecked")
  public List<String> getLogs() {
    Object obj = object.get("logs");
    if (obj instanceof List) {
      return (List<String>) obj;
    }
    return null;
  }

  public void setLogs(List<String> logs) {
    object.put("logs", logs);
  }

}
