package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.mongodb.BasicDBObject;

@Service
public class ServiceDataStore extends AbstractDataStore<DOService> {

  @Override
  protected String getCollectionName() {
    return "service";
  }

  @Override
  protected DOService createEmptyDomainObject() {
    DOService service = new DOService();
    service.setStatus(DOService.STOPPED);
    return service;
  }

  @Override
  protected DOService createDomainObject(BasicDBObject obj) {
    return new DOService(obj);
  }

  public DOService findByName(String name) {
    return findOne("name", name);
  }

}
