package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceLogDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOServiceLog;

@RestController
@RequestMapping(value = "/rest/servicelog")
public class ServiceLogController extends AbstractController<DOServiceLog> {

  @Inject
  ServiceLogDataStore store;

  @Override
  protected AbstractDataStore<DOServiceLog> getStore() {
    return store;
  }
}
