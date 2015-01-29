package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.service.DockerService;

@RestController
@RequestMapping(value = "/rest/service")
public class ServiceController extends AbstractController<DOService> {

  @Inject
  ServiceDataStore store;

  @Inject
  DockerService dockerService;

  @RequestMapping(method = RequestMethod.POST, value = "/start")
  public void start(@RequestBody DOService obj) throws IOException {
    dockerService.startService(obj);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/stop")
  public void stop(@RequestBody DOService obj) throws IOException {
    dockerService.stopService(obj);
  }

  @Override
  protected AbstractDataStore<DOService> getStore() {
    return store;
  }
}
