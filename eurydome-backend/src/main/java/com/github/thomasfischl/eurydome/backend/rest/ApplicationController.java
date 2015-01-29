package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ApplicationDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;

@RestController
@RequestMapping(value = "/rest/application")
public class ApplicationController extends AbstractController<DOApplication> {

  @Inject
  ApplicationDataStore store;

  @Override
  protected AbstractDataStore<DOApplication> getStore() {
    return store;
  }

}
