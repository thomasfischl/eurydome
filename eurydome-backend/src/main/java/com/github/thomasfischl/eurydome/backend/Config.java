package com.github.thomasfischl.eurydome.backend;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;

@Configuration
public class Config {

  @Bean
  public MongoDbDataStore getStoreBean() throws IOException {
    MongoDbDataStore store = new MongoDbDataStore();
    store.confgiure(new DODatabaseConfiguration("192.168.59.103", "27017"));
    return store;
  }

}
