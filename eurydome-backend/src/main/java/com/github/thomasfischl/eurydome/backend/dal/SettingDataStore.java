package com.github.thomasfischl.eurydome.backend.dal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOSetting;
import com.mongodb.BasicDBObject;

@Service
public class SettingDataStore extends AbstractDataStore<DOSetting> {

  @Inject
  public MongoDbDataStore store;

  @PostConstruct
  public void init() {
    init(store);
  }

  @Override
  protected String getCollectionName() {
    return "setting";
  }

  @Override
  protected DOSetting createEmptyDomainObject() {
    return new DOSetting();
  }

  @Override
  protected DOSetting createDomainObject(BasicDBObject obj) {
    return new DOSetting(obj);
  }

  public DOSetting findByKey(String key) {
    return findOne("key", key);
  }

}
