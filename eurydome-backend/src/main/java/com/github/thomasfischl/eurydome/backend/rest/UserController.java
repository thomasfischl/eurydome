package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.thomasfischl.eurydome.backend.DataStore;

@RestController
public class UserController {

  @RequestMapping("/user/companies")
  public List<String> getCompanies() {
    ArrayList<String> result = new ArrayList<String>();
    result.add("Borland");
    result.add("Microfocus");
    return result;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/user/mapping")
  public ModelAndView addMapping(@RequestParam(value = "company") String company, @RequestParam(value = "url") String url) {
    DataStore.getInstance().addUrlMapping(company, url);
    return new ModelAndView("redirect:/admin.html");
  }

  @RequestMapping(method = RequestMethod.GET, value = "/user/mappings")
  public Map<String, String> getMappings() {
    return DataStore.getInstance().getUrlMapping();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public void login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password,
      @RequestParam(value = "company") String company, HttpServletRequest req, HttpServletResponse resp) throws IOException {

    String subdomain = getSubDomains().get(company);
    if (subdomain == null) {
      resp.sendRedirect("/");
      return;
    }

    if ("thomas.fischl@borland.com".equalsIgnoreCase(email)) {
      // String baseUrl = req.getScheme() + "://" + req.getServerName() +
      // ":" + req.getServerPort();
      // resp.sendRedirect(baseUrl + "/" + subdomain +
      // "/login?userName=admin&passWord=admin");
      resp.sendRedirect("/" + subdomain + ".html");
      return;
    }

    resp.sendRedirect("/");

  }

  private Map<String, String> getSubDomains() {
    Map<String, String> subdomains = new HashMap<String, String>();
    subdomains.put("Borland", "scc01");
    subdomains.put("Microfocus", "scc02");
    return subdomains;
  }

}
