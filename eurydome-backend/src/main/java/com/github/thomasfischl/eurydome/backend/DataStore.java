package com.github.thomasfischl.eurydome.backend;

import java.util.HashMap;
import java.util.Map;

public class DataStore {

  private static DataStore singelton;

  private Map<String, String> urlMapping = new HashMap<String, String>();

  private DataStore() {
  }

  public void addUrlMapping(String company, String url) {
    urlMapping.put(company, url);
  }

  public String getUrlForCompany(String company) {
    return urlMapping.get(company);
  }
  
  public Map<String, String> getUrlMapping() {
    return urlMapping;
  }

  public static DataStore getInstance() {
    if (singelton == null) {
      singelton = new DataStore();
    }
    return singelton;
  }

}
