package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.SettingDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOSetting;
import com.github.thomasfischl.eurydome.backend.service.ProxyService;

@RestController
@RequestMapping(value = "/rest/setting")
public class SettingController extends AbstractController<DOSetting> {

  @Inject
  SettingDataStore store;

  @Inject
  ProxyService proxyService;

  private void init() {
    if (store.isConnected()) {
      if (store.findByName(DOSetting.SETTING_DOCKER_CERTS) == null) {
        DOSetting object = store.createObject();
        object.setName(DOSetting.SETTING_DOCKER_CERTS);
        object.setValue(null);
        store.save(object);
      }
      if (store.findByName(DOSetting.SETTING_DOCKER_HOST) == null) {
        DOSetting object = store.createObject();
        object.setName(DOSetting.SETTING_DOCKER_HOST);
        object.setValue(null);
        store.save(object);
      }
    }
  }

  @Override
  public List<DOSetting> listAll() {
    init();
    return super.listAll();
  }
  
  @Override
  public DOSetting find(String id, String name) {
    init();
    return super.find(id, name);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/getProxyConfiguration")
  public String getProxyConfiguration() throws IOException {
    return proxyService.getConfiguration();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/saveProxyConfiguration")
  public void saveProxyConfiguration() throws IOException {
    proxyService.updateConfiguration();
    proxyService.reloadProxy();
  }

  @Override
  protected AbstractDataStore<DOSetting> getStore() {
    return store;
  }

}
