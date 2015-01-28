package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.ApplicationDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;

@RestController
public class ApplicationController {

  @Inject
  ApplicationDataStore store;

  @RequestMapping(method = RequestMethod.GET, value = "/rest/application/list")
  public List<DOApplication> listAll() {
    return store.findAll();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/rest/application/create")
  public DOApplication create() {
    return store.createObject();
  }

  @RequestMapping(value = "/rest/application/save")
  public void save(@RequestBody DOApplication obj) {
    store.save(obj);
    obj.getId();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/rest/application/test")
  public void test() {
    DOApplication app = store.createObject();

    System.out.println("ID: " + app.getId());

    app.setLocation("http://localhost");
    app.setName("Silk Central");

    store.save(app);

    for (DOApplication obj : store.findAll()) {
      System.out.println(obj.getId() + " " + obj.getName());
    }

  }

}
