package com.github.thomasfischl.eurydome.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class DOServiceLog extends AbstractDomainObject {

  public final static String STATUS_RUNNING = "running";
  public final static String STATUS_FINISHED = "finished";
  public final static String STATUS_FAILED = "failed";

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
    return new ArrayList<String>();
  }

  public void setLogs(List<String> logs) {
    object.put("logs", logs);
  }

  public void setStatus(String status) {
    object.put("status", status);
  }

  public String getStatus() {
    return object.getString("status");
  }

  public void setStep(String step) {
    object.put("step", step);
  }

  public String getStep() {
    return object.getString("step");
  }

  public void setTotalSteps(String totalSteps) {
    object.put("totalSteps", totalSteps);
  }

  public String getTotalSteps() {
    return object.getString("totalSteps");
  }

}
