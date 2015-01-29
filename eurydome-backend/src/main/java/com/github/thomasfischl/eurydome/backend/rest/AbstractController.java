package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.model.AbstractDomainObject;

public abstract class AbstractController<T extends AbstractDomainObject> {

  protected abstract AbstractDataStore<T> getStore();

  @RequestMapping(method = RequestMethod.GET, value = "/list")
  public List<T> listAll() {
    return getStore().findAll();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/find")
  public T find(@RequestParam(value = "id", required = false) String id,
      @RequestParam(value = "name", required = false) String name) {
    if (id != null) {
      return getStore().findById(id);
    } else if (name != null) {
      return getStore().findByName(name);
    } else {
      throw new IllegalArgumentException("The method need either an id parameter or a name parameter.");
    }
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
