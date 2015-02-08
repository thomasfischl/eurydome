package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.TaskDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOTask;

@RestController
@RequestMapping(value = "/rest/task")
public class TaskController extends AbstractController<DOTask> {

  @Inject
  TaskDataStore store;

  @Override
  protected AbstractDataStore<DOTask> getStore() {
    return store;
  }
}
