package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOTask;
import com.mongodb.BasicDBObject;

@Service
public class TaskDataStore extends AbstractDataStore<DOTask> {

  @Override
  protected String getCollectionName() {
    return "task";
  }

  @Override
  protected DOTask createEmptyDomainObject() {
    DOTask task = new DOTask();
    task.setCompleted(false);
    task.setStep(0);
    task.setTotalSteps(0);
    task.setCreatedAt(System.currentTimeMillis());
    task.setFinishedAt(-1);
    return task;
  }

  @Override
  protected DOTask createDomainObject(BasicDBObject obj) {
    return new DOTask(obj);
  }

}
