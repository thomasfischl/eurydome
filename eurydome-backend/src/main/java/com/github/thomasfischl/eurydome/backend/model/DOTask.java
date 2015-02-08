package com.github.thomasfischl.eurydome.backend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;

public class DOTask extends AbstractDomainObject {

  public final static String STATUS_WAITING = "waiting";
  public final static String STATUS_RUNNING = "running";
  public final static String STATUS_FINISHED = "finished";
  public final static String STATUS_FAILED = "failed";

  public DOTask() {
    super();
  }

  public DOTask(BasicDBObject object) {
    super(object);
  }

  @Override
  public void validate() {
    super.validate();
    if (getLogOutput() == null) {
      setLogOutput(new ArrayList<String>());
    }
  }

  @SuppressWarnings("unchecked")
  public List<String> getLogOutput() {
    Object obj = object.get("logOutput");
    if (obj instanceof List) {
      return (List<String>) obj;
    }
    return new ArrayList<String>();
  }

  public void setLogOutput(List<String> logOutput) {
    object.put("logOutput", logOutput);
  }

  public void addLogOutput(String message) {
    List<String> logOutput = getLogOutput();
    logOutput.add(message);
    object.put("logOutput", logOutput);
  }

  public void setStatus(String status) {
    object.put("status", status);
  }

  public String getStatus() {
    return object.getString("status");
  }

  public void setStep(int step) {
    object.put("step", step);
  }

  public int getStep() {
    return object.getInt("step");
  }

  public void setStepName(String stepName) {
    object.put("stepName", stepName);
  }

  public String getStepName() {
    return object.getString("stepName");
  }

  public void setTotalSteps(int totalSteps) {
    object.put("totalSteps", totalSteps);
  }

  public int getTotalSteps() {
    return object.getInt("totalSteps");
  }

  public String getTaskType() {
    return object.getString("taskType");
  }

  public void setTaskType(String taskType) {
    object.put("taskType", taskType);
  }

  public boolean isCompleted() {
    return object.getBoolean("completed");
  }

  public void setCompleted(boolean completed) {
    object.put("completed", completed);
  }

  @SuppressWarnings("unchecked")
  public Map<String, String> getSettings() {
    Map<String, String> settings = (Map<String, String>) object.get("settings");
    if (settings == null) {
      return new HashMap<String, String>();
    }
    return settings;
  }

  public void setSettings(Map<String, String> settings) {
    object.put("settings", settings);
  }

  public String getSetting(String key) {
    return getSettings().get(key);
  }

  public void setSetting(String key, String value) {
    Map<String, String> settings = getSettings();
    settings.put(key, value);
    setSettings(settings);
  }

  public long getCreatedAt() {
    return object.getLong("createdAt", -1);
  }

  public void setCreatedAt(long createdAt) {
    object.put("createdAt", createdAt);
  }

  public long getFinishedAt() {
    return object.getLong("finishedAt", -1);
  }

  public void setFinishedAt(long finishedAt) {
    object.put("finishedAt", finishedAt);
  }

}
