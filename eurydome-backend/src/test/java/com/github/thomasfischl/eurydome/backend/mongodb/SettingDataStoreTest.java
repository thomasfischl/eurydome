package com.github.thomasfischl.eurydome.backend.mongodb;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.dal.SettingDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;
import com.github.thomasfischl.eurydome.backend.model.DOSetting;

public class SettingDataStoreTest {

  @Test
  public void testCRUD() throws Exception {
    SettingDataStore store = createStore();

    // Create Setting
    DOSetting setting = store.createObject();
    setting.setKey("Test.Docker.Hostname");
    setting.setValue("https://localhost:1234");

    store.save(setting);

    // Find Setting
    DOSetting setting2 = store.findByKey("Test.Docker.Hostname");
    Assert.assertNotNull(setting2);
    Assert.assertEquals("https://localhost:1234", setting2.getValue());

    // Delete Setting
    store.remove(setting2);
    setting2 = store.findByKey("Test.Docker.Hostname");
    Assert.assertNull(setting2);
  }

  @Test
  public void testFindAll() throws IOException {
    SettingDataStore store = createStore();

    List<DOSetting> settings = store.findAll();
    for (DOSetting obj : settings) {
      System.out.println(obj.getKey() + " => " + obj.getValue());
    }
  }

  private SettingDataStore createStore() throws IOException {
    MongoDbDataStore client = new MongoDbDataStore();
    client.confgiure(new DODatabaseConfiguration("192.168.59.103", "27017"));

    SettingDataStore store = new SettingDataStore();
    store.store = client;
    return store;
  }

}
