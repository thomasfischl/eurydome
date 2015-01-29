package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.OrganisationDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOOrganisation;

@RestController
@RequestMapping(value = "/rest/organisation")
public class OrganisationController extends AbstractController<DOOrganisation> {

  @Inject
  OrganisationDataStore store;

  @Override
  protected AbstractDataStore<DOOrganisation> getStore() {
    return store;
  }

}
