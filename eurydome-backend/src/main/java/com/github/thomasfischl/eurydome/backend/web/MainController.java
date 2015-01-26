package com.github.thomasfischl.eurydome.backend.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

  @RequestMapping("/")
  public String loginPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (ServletHelper.isUserAuthenticated(req)) {
      resp.sendRedirect("/app.html");
    }
    return "index.html";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/admin/login")
  public void login(HttpServletRequest req, HttpServletResponse resp,
      @RequestParam(value = "username") String username, @RequestParam(value = "password") String password)
      throws IOException {

    System.out.println("user: " + username);
    System.out.println("password: " + password);

    if ("test".equals(username)) {
      ServletHelper.setUserSession(req);
      resp.sendRedirect("/app.html");
    } else {
      resp.sendError(406);
    }
  }

//  @RequestMapping("/app")
//  public String appPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//    if (!ServletHelper.isUserAuthenticated(req)) {
//      return "redirect:/";
//    }
//
//    return "app.html";
//  }
}