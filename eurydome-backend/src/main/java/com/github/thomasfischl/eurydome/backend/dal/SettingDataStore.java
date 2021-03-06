package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOSetting;
import com.mongodb.BasicDBObject;

@Service
public class SettingDataStore extends AbstractDataStore<DOSetting> {

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

}
