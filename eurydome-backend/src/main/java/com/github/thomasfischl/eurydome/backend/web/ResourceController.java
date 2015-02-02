package com.github.thomasfischl.eurydome.backend.web;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResourceController {

  @RequestMapping(value = { "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.dart", "/**/*.woff", "/**/*.ttf", "/**/*.gif" })
  public void reverse(HttpServletRequest req, HttpServletResponse resp) {
    try {
      System.out.println("Load from pub serve: " + req.getServletPath());
      URLConnection connection = new URL("http://localhost:8081" + req.getServletPath()).openConnection();
      connection.connect();
      IOUtils.copy(connection.getInputStream(), resp.getOutputStream());
      resp.flushBuffer();
    } catch (IOException e) {
      System.out.println("Unkown resource. " + e.getMessage());
    }
  }

}