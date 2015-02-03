package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.mongodb.BasicDBObject;

@Service
public class DockerHostDataStore extends AbstractDataStore<DODockerHost> {

  @Override
  protected String getCollectionName() {
    return "dockerHost";
  }

  @Override
  protected DODockerHost createEmptyDomainObject() {
    return new DODockerHost();
  }

  @Override
  protected DODockerHost createDomainObject(BasicDBObject obj) {
    return new DODockerHost(obj);
  }

}
