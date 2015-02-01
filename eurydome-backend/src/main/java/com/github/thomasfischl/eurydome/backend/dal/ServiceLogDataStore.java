package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOServiceLog;
import com.mongodb.BasicDBObject;

@Service
public class ServiceLogDataStore extends AbstractDataStore<DOServiceLog> {

  @Override
  protected String getCollectionName() {
    return "servicelog";
  }

  @Override
  protected DOServiceLog createEmptyDomainObject() {
    return new DOServiceLog();
  }

  @Override
  protected DOServiceLog createDomainObject(BasicDBObject obj) {
    return new DOServiceLog(obj);
  }

}
