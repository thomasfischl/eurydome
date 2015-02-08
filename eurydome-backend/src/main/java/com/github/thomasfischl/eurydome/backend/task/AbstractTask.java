package com.github.thomasfischl.eurydome.backend.task;

import java.util.List;

import javax.inject.Inject;

import com.github.thomasfischl.eurydome.backend.dal.TaskDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOTask;

public abstract class AbstractTask {

  @Inject
  protected TaskDataStore taskDataStore;

  private DOTask task;

  public void prepare(DOTask task) {
    this.task = task;
  }

  public abstract List<StepDefinition> getSteps();

  public void begin() {
  }

  public void complete() {
    task = taskDataStore.findById(task.getId());
    task.setStatus(DOTask.STATUS_FINISHED);
    task.setCompleted(true);
    taskDataStore.save(task);
  }

  public void completeWithError(String errorMsg) {
    task = taskDataStore.findById(task.getId());
    task.setStatus(DOTask.STATUS_FAILED);
    task.addLogOutput("Error: " + errorMsg);
    task.setCompleted(true);
    taskDataStore.save(task);
  }

  protected String getSetting(String key) {
    return task.getSetting(key);
  }

  protected void logMessage(String... lines) {
    if (lines != null) {
      task = taskDataStore.findById(task.getId());
      for (String line : lines) {
        task.addLogOutput(line);
      }
      taskDataStore.save(task);
    }
  }

}
