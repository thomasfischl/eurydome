package com.github.thomasfischl.eurydome.backend.dal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.mongodb.BasicDBObject;

@Service
public class ServiceDataStore extends AbstractDataStore<DOService> {

  @Inject
  public MongoDbDataStore store;

  @PostConstruct
  public void init() {
    init(store);
  }

  @Override
  protected String getCollectionName() {
    return "service";
  }

  @Override
  protected DOService createEmptyDomainObject() {
    return new DOService();
  }

  @Override
  protected DOService createDomainObject(BasicDBObject obj) {
    return new DOService(obj);
  }

  public DOService findByName(String name) {
    return findOne("name", name);
  }

}
