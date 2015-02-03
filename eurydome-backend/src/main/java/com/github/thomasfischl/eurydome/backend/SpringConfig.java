package com.github.thomasfischl.eurydome.backend;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;

@Configuration
public class SpringConfig {

  private final static Log LOG = LogFactory.getLog(SpringConfig.class);

  @Bean
  public MongoDbDataStore getStoreBean() throws IOException {
    MongoDbDataStore store = new MongoDbDataStore();
    try {
      String host = System.getenv(DODatabaseConfiguration.EURYDOME_DB_HOST);
      String port = System.getenv(DODatabaseConfiguration.EURYDOME_DB_PORT);

      if (StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(port)) {
        LOG.info("Use environment variables for database configuration.");
        store.confgiure(new DODatabaseConfiguration(host, port));
      } else {
        DODatabaseConfiguration config = store.loadDatabaseSettings();
        if (config != null) {
          LOG.info("Use temporary configuration file for database configuration.");
          store.confgiure(config);
        } else {
          LOG.info("Use default settings for database configuration.");
          store.confgiure(new DODatabaseConfiguration("localhost", "27017"));
        }
      }
    } catch (Exception e) {
      LOG.fatal("Database not connected.", e);
    }
    return store;
  }

}
