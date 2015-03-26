package com.github.thomasfischl.eurydome.backend.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
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

  public void prepare() {

    List<String> availableSettings = new ArrayList<String>();
    availableSettings.add(DOSetting.LOAD_BALANCER_URL);

    for (String setting : availableSettings) {
      if (find(null, setting) == null) {
        DOSetting settingObj = create();
        settingObj.setName(setting);
        save(settingObj);
      }
    }

  }

  @Override
  public List<DOSetting> listAll() {
    prepare();
    return super.listAll();
  }

  @Override
  public DOSetting find(String id, String name) {
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

  @RequestMapping(method = RequestMethod.GET, value = "/getServerLog")
  public String getServerLog() throws IOException {
    return FileUtils.readFileToString(new File("spring.log"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/getSystemEnvironment")
  public String getSystemEnvironment() throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("Environment Variables:\n");
    sb.append("--------------------------------------\n\n");
    for (Entry<String, String> entry : System.getenv().entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    sb.append("\n\n");
    sb.append("System Properties:\n");
    sb.append("--------------------------------------\n\n");
    for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    return sb.toString();
  }

  @Override
  protected AbstractDataStore<DOSetting> getStore() {
    return store;
  }

}
