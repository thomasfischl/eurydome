package com.github.thomasfischl.eurydome.backend.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.dal.TaskDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOTask;
import com.github.thomasfischl.eurydome.backend.task.AbstractTask;
import com.github.thomasfischl.eurydome.backend.task.StepDefinition;

@Service
public class TaskService implements ApplicationContextAware {

  private final static Log LOG = LogFactory.getLog(TaskService.class);

  private ApplicationContext ctx;

  @Inject
  private TaskDataStore taskDataStore;

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.ctx = ctx;
  }

  @Scheduled(fixedDelay = 5000)
  public void execute() {
    if (!taskDataStore.isConnected()) {
      return;
    }

    List<DOTask> tasks = taskDataStore.findAll();

    for (DOTask task : tasks) {
      if (!task.isCompleted()) {
        executeTask(task);
      }
    }
  }

  public void executeTask(DOTask task) {
    AbstractTask taskInstance;
    try {
      taskInstance = ctx.getBean(task.getTaskType(), AbstractTask.class);
    } catch (BeansException e) {
      LOG.error("Can't find task with name '" + task.getTaskType() + "'.", e);
      task.addLogOutput("Can't find task with name '" + task.getTaskType() + "'.");
      task.setStatus(DOTask.STATUS_FAILED);
      taskDataStore.save(task);
      return;
    }
    taskInstance.prepare(task);
    List<StepDefinition> steps = taskInstance.getSteps();

    int totalStepCount = steps.size();
    task.setTotalSteps(totalStepCount);
    task.setStep(0);
    task.setStatus(DOTask.STATUS_RUNNING);
    taskDataStore.save(task);

    taskInstance.begin();

    int step = 1;
    for (StepDefinition stepDefinition : steps) {
      try {
        task = taskDataStore.findById(task.getId());
        task.setStep(step);
        task.setStepName(stepDefinition.getName());
        task.addLogOutput("Execute Step " + step + ": " + stepDefinition.getName());
        taskDataStore.save(task);
        
        stepDefinition.getTask().run();
        step++;
      } catch (Exception e) {
        LOG.error("Error occurs during execution step '" + step + ": " + stepDefinition.getName() + "'.", e);
        task.addLogOutput(e.getMessage());
        task.setStatus(DOTask.STATUS_FAILED);
        taskDataStore.save(task);
        taskInstance.completeWithError(e.getMessage());
        return;
      }
    }

    taskInstance.complete();
  }
}
