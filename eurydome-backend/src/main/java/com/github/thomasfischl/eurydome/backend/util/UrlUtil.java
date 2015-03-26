package com.github.thomasfischl.eurydome.backend.util;

public class UrlUtil {

  public static String concatUrl(String url, String path) {
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 2);
    }
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    return url + "/" + path;
  }

}
