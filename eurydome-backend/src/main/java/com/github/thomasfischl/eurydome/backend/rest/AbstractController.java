package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.model.AbstractDomainObject;

public abstract class AbstractController<T extends AbstractDomainObject> {

  protected abstract AbstractDataStore<T> getStore();

  @RequestMapping(method = RequestMethod.GET, value = "/list")
  public List<T> listAll() {
    return getStore().findAll();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/find/{id}")
  public T find(@PathVariable("id") String id) {
    return getStore().findById(id);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/create")
  public T create() {
    return getStore().createObject();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/save")
  public void save(@RequestBody T obj) {
    getStore().save(obj);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/delete")
  public void remove(@RequestBody T obj) {
    getStore().remove(obj);
  }

  @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteAll")
  public void removeAll() {
    getStore().removeAll();
  }

}
