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

  @RequestMapping(method = RequestMethod.POST, value = "/rest/application/create")
  public DOApplication create() {
    return store.createObject();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/rest/application/save")
  public void save(@RequestBody DOApplication obj) {
    store.save(obj);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/rest/application/delete")
  public void remove(@RequestBody DOApplication obj) {
    store.remove(obj);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/rest/application/deleteAll")
  public void test() {
    store.removeAll();
  }

}
