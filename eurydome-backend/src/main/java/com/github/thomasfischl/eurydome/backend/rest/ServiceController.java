package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOService;

@RestController
@RequestMapping(value = "/rest/service")
public class ServiceController {

  @Inject
  ServiceDataStore store;

  @RequestMapping(method = RequestMethod.GET, value = "/list")
  public List<DOService> listAll() {
    return store.findAll();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/find/{id}")
  public DOService find(@PathVariable("id") String id) {
    return store.findById(id);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/create")
  public DOService create() {
    return store.createObject();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/save")
  public void save(@RequestBody DOService obj) {
    store.save(obj);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/delete")
  public void remove(@RequestBody DOService obj) {
    store.remove(obj);
  }

  @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteAll")
  public void test() {
    store.removeAll();
  }
}
