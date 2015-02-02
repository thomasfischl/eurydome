package com.github.thomasfischl.eurydome.backend;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;

@Configuration
public class SpringConfig {

  @Bean
  public MongoDbDataStore getStoreBean() throws IOException {
    MongoDbDataStore store = new MongoDbDataStore();
    try {
      DODatabaseConfiguration config = store.loadDatabaseSettings();
      if(config!=null){
        store.confgiure(config);
      }else{
        store.confgiure(new DODatabaseConfiguration("localhost", "27017"));
      }
    } catch (Exception e) {
      System.err.println("Database not connected.");
      e.printStackTrace();
    }
    return store;
  }

}
