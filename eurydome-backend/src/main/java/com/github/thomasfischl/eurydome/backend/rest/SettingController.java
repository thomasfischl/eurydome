package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;

import javax.annotation.PostConstruct;
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

  @PostConstruct
  public void init() {
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

  @RequestMapping(method = RequestMethod.GET, value = "/proxyConfiguration")
  public String getProxyConfiguration() throws IOException {
    return proxyService.getConfiguration();
  }

  @Override
  protected AbstractDataStore<DOSetting> getStore() {
    return store;
  }

}
