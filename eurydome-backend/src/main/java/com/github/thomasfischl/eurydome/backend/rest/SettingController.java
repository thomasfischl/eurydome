package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.SettingDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOSetting;

@RestController
@RequestMapping(value = "/rest/setting")
public class SettingController {

  @Inject
  SettingDataStore store;

  @PostConstruct
  public void init(){
    if(store.findByKey(DOSetting.SETTING_DOCKER_CERTS)==null){
      DOSetting object = store.createObject();
      object.setKey(DOSetting.SETTING_DOCKER_CERTS);
      object.setValue(null);
      store.save(object);
    }
    if(store.findByKey(DOSetting.SETTING_DOCKER_HOST)==null){
      DOSetting object = store.createObject();
      object.setKey(DOSetting.SETTING_DOCKER_HOST);
      object.setValue(null);
      store.save(object);
    }
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/list")
  public List<DOSetting> listAll() {
    return store.findAll();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/create")
  public DOSetting create() {
    return store.createObject();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/save")
  public void save(@RequestBody DOSetting obj) {
    store.save(obj);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/delete")
  public void remove(@RequestBody DOSetting obj) {
    store.remove(obj);
  }

  @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteAll")
  public void test() {
    store.removeAll();
  }

}
