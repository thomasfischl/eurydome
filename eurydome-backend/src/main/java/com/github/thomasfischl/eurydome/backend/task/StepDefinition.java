package com.github.thomasfischl.eurydome.backend.task;

public class StepDefinition {

  private final String name;
  private final Runnable task;

  public StepDefinition(String name, Runnable task) {
    this.name = name;
    this.task = task;
  }

  public String getName() {
    return name;
  }

  public Runnable getTask() {
    return task;
  }

}
