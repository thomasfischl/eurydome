package com.github.thomasfischl.eurydome.backend.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.AbstractDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ApplicationDataStore;
import com.github.thomasfischl.eurydome.backend.dal.OrganisationDataStore;
import com.github.thomasfischl.eurydome.backend.dal.ServiceDataStore;
import com.github.thomasfischl.eurydome.backend.dal.UserDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.github.thomasfischl.eurydome.backend.model.DOOrganisation;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.model.DOUser;
import com.github.thomasfischl.eurydome.backend.util.HashUtil;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController extends AbstractController<DOUser> {

  private final static String AUTH_TOKEN = "AUTH_TOKEN";

  @Inject
  UserDataStore store;

  @Inject
  ApplicationDataStore applicationStore;

  @Inject
  OrganisationDataStore organisationStore;

  @Inject
  ServiceDataStore serviceStore;

  @Override
  protected AbstractDataStore<DOUser> getStore() {
    return store;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public boolean login(@RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password, HttpServletRequest req) {

    HttpSession session = req.getSession(true);

    if ("admin".equals(username) && "admin".equals(password)) {
      session.setAttribute(AUTH_TOKEN, true);
      return true;
    }

    DOUser user = getStore().findByName(username);
    if (user == null) {
      return false;
    }
    if (!user.isAdmin()) {
      return false;
    }

    boolean success = user.getPassword() == encryptPassword(username, password);
    session.setAttribute(AUTH_TOKEN, success);
    return success;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/registration")
  public boolean registration(@RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password, @RequestParam(value = "organisation") String organisation,
      @RequestParam(value = "application") String application) {

    DOUser user = getStore().findByName(username);
    if (user != null) {
      throw new IllegalStateException("User already exits!");
    }

    DOApplication applicationObj = applicationStore.findById(application);
    if (applicationObj == null) {
      throw new IllegalStateException("No application with id '" + application + "' found.");
    }

    DOOrganisation org = organisationStore.findByName(organisation);
    if (org == null) {
      org = organisationStore.createObject();
      org.setName(organisation);
      organisationStore.save(org);
    }

    user = getStore().createObject();
    user.setAdmin(false);
    user.setName(username);
    user.setOrganisationRef(org.getId());
    user.setPassword(encryptPassword(username, password));
    getStore().save(user);

    boolean serviceAlreadyExits = false;
    for (String service : org.getServices()) {
      DOService serviceObj = serviceStore.findById(service);
      if (serviceObj != null && application.equals(serviceObj.getApplicationRef())) {
        serviceAlreadyExits = true;
        break;
      }
    }

    //
    // create new service, if no one exits for the organisation
    //
    if (!serviceAlreadyExits) {
      DOService service = serviceStore.createObject();
      service.setName(applicationObj.getName() + " - " + org.getName());
      service.setApplicationRef(applicationObj.getId());
      service.setUrl(createServiceUrl(applicationObj.getBaseServiceUrl()));
      serviceStore.save(service);

      List<String> services = org.getServices();
      services.add(service.getId());
      org.setServices(services);
      organisationStore.save(org);
    }

    return true;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/authenticated")
  public boolean authenticated(HttpServletRequest req) {
    HttpSession session = req.getSession(true);
    Object attribute = session.getAttribute(AUTH_TOKEN);
    if (attribute instanceof Boolean) {
      return (boolean) attribute;
    }
    return false;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/logout")
  public void logout(HttpServletRequest req) {
    HttpSession session = req.getSession(true);
    session.invalidate();
  }

  @Override
  public List<DOUser> listAll() {
    List<DOUser> users = super.listAll();
    users.forEach((obj) -> obj.setPassword(null));
    return users;
  }

  @Override
  public DOUser find(@RequestParam(value = "id", required = false) String id,
      @RequestParam(value = "name", required = false) String name) {
    DOUser user = super.find(id, name);
    user.setPassword(null);
    return user;
  }

  @Override
  public void save(@RequestBody DOUser obj) {
    if (obj.getPassword() == null) {
      DOUser user = getStore().findById(obj.getId());
      if (user != null) {
        obj.setPassword(user.getPassword());
      }
    } else {
      obj.setPassword(encryptPassword(obj.getName(), obj.getPassword()));
    }
    super.save(obj);
  }

  private String encryptPassword(String username, String password) {
    return HashUtil.generateSHA256(username + "a§D)($LCalk235790aj02" + password);
  }

  private String createServiceUrl(String baseServiceUrl) {
    if (StringUtils.isEmpty(baseServiceUrl)) {
      baseServiceUrl = "service";
    }

    int maxId = 0;
    List<DOService> services = serviceStore.findAll();
    for (DOService service : services) {
      if (service.getUrl() != null && service.getUrl().startsWith(baseServiceUrl)) {
        String url = service.getUrl();
        String suffixUrl = url.substring(baseServiceUrl.length() - 1);
        try {
          int id = Integer.parseInt(suffixUrl);
          maxId = Math.max(id, maxId);
        } catch (Exception e) {
          // nothing to do
        }
      }
    }
    return baseServiceUrl + (maxId++);
  }
}
