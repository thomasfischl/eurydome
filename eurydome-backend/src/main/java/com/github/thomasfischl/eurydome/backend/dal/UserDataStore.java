package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOUser;
import com.mongodb.BasicDBObject;

@Service
public class UserDataStore extends AbstractDataStore<DOUser> {

  @Override
  protected String getCollectionName() {
    return "user";
  }

  @Override
  protected DOUser createEmptyDomainObject() {
    return new DOUser();
  }

  @Override
  protected DOUser createDomainObject(BasicDBObject obj) {
    return new DOUser(obj);
  }

}
