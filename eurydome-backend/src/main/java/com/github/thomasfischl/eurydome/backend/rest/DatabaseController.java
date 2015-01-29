package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;

@RestController
@RequestMapping(value = "/rest/database")
public class DatabaseController {

  @Inject
  MongoDbDataStore store;

  @RequestMapping(method = RequestMethod.GET, value = "/get")
  public DODatabaseConfiguration get() {
    return store.getConfiguration();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/save")
  public void save(@RequestBody DODatabaseConfiguration obj) {
    store.confgiure(obj);
  }

}
