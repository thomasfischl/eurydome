package com.github.thomasfischl.eurydome.demoapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResourceController {

  @RequestMapping(value = "/detail")
  public void reverse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    PrintWriter writer = resp.getWriter();
    writer.write("Path: " + req.getServletPath() + "\n");
    writer.write("Host: " + InetAddress.getLocalHost() + "\n");

    
    writer.close();
  }

}