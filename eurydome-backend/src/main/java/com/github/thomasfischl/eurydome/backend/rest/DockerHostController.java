package com.github.thomasfischl.eurydome.backend.rest;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.DockerHostDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.github.thomasfischl.eurydome.backend.service.DockerService;

@RestController
@RequestMapping(value = "/rest/dockerhost")
public class DockerHostController extends AbstractController<DODockerHost> {

  private final static Log LOG = LogFactory.getLog(DockerHostController.class);

  
  @Inject
  DockerHostDataStore store;

  @Inject
  DockerService dockerSerivce;

  @Override
  protected AbstractDataStore<DODockerHost> getStore() {
    return store;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/test")
  public String testConnection(@RequestParam(value = "id") String id) {
    try {
      DODockerHost dockerhost = store.findById(id);
      if (dockerhost == null) {
        throw new IllegalArgumentException("No docker host with id '" + id + "' found.");
      }
      dockerSerivce.testConnection(dockerhost);
    } catch (Exception e) {
      LOG.error(e);
      return "Error: " + e.getMessage();
    }
    return "OK";
  }

}
