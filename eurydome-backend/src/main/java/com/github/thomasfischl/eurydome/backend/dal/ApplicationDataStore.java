package com.github.thomasfischl.eurydome.backend.dal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.mongodb.BasicDBObject;

@Service
public class ApplicationDataStore extends AbstractDataStore<DOApplication> {

  @Inject
  public MongoDbDataStore store;

  @PostConstruct
  public void init() {
    init(store);
  }

  @Override
  protected String getCollectionName() {
    return "application";
  }

  @Override
  protected DOApplication createEmptyDomainObject() {
    return new DOApplication();
  }

  @Override
  protected DOApplication createDomainObject(BasicDBObject obj) {
    return new DOApplication(obj);
  }

}
