package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.UserDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOUser;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController extends AbstractController<DOUser> {

  @Inject
  UserDataStore store;

  @Override
  protected AbstractDataStore<DOUser> getStore() {
    return store;
  }

}
