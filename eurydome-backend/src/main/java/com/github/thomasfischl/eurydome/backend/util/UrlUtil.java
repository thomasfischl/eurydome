package com.github.thomasfischl.eurydome.backend.util;

public class UrlUtil {

  public static String concatUrl(String url, String path) {
    if (url.endsWith("/")) {
      return url + path;
    } else {
      return url + "/" + path;
    }
  }

}
