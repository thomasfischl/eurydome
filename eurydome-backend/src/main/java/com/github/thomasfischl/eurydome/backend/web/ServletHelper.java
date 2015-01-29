package com.github.thomasfischl.eurydome.backend.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletHelper {

  private final static String USER = "USER";

  public static boolean isUserAuthenticated(HttpServletRequest req) {
    HttpSession session = req.getSession();
    if (session != null) {
      Object userSession = session.getAttribute(USER);
      if (userSession instanceof UserSession) {
        return true;
      }
    }
    return false;
  }

  public static void setUserSession(HttpServletRequest req) {
    HttpSession session = req.getSession(true);
    session.setAttribute(USER, new UserSession());
  }

  public static void invalidateSession(HttpServletRequest req) {
    HttpSession session = req.getSession(true);
    session.invalidate();
  }

  public static void checkLogin(HttpServletRequest req, HttpServletResponse resp) {
    try {
      resp.sendRedirect("/index.html");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
