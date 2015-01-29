package com.github.thomasfischl.eurydome.backend.dal;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOOrganisation;
import com.mongodb.BasicDBObject;

@Service
public class OrganisationDataStore extends AbstractDataStore<DOOrganisation> {

  @Override
  protected String getCollectionName() {
    return "organisation";
  }

  @Override
  protected DOOrganisation createEmptyDomainObject() {
    return new DOOrganisation();
  }

  @Override
  protected DOOrganisation createDomainObject(BasicDBObject obj) {
    return new DOOrganisation(obj);
  }

}
