package com.github.thomasfischl.eurydome.backend.dal;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;

public class MongoDbDataStore {

  private final static Log LOG = LogFactory.getLog(MongoDbDataStore.class);

  private MongoClient mongoClient;
  private DB db;
  private DODatabaseConfiguration configuration;
  private File dbConfigFile = new File(FileUtils.getTempDirectory(), "databaseConfiguration.json");

  public void confgiure(DODatabaseConfiguration config) {
    this.configuration = config;

    try {
      MongoClientOptions options = MongoClientOptions.builder().connectTimeout(1000).build();
      mongoClient = new MongoClient(new ServerAddress(config.getHost(), config.getPortAsInt()), options);
      db = mongoClient.getDB("eurydome");
      db.getStats();
      storeDatabaseSettings();
    } catch (Exception e) {
      mongoClient = null;
      db = null;
      throw new RuntimeException(e);
    }
  }

  public DBCollection getCollection(String name) {
    if (db == null) {
      throw new IllegalStateException("No database connection");
    }
    return db.getCollection(name);
  }

  public GridFS getGridFs() {
    if (db == null) {
      throw new IllegalStateException("No database connection");
    }
    return new GridFS(db);
  }

  public DODatabaseConfiguration getConfiguration() {
    return configuration;
  }

  public boolean isConnected() {
    return mongoClient != null;
  }

  public void storeDatabaseSettings() {
    try {
      if (dbConfigFile.exists()) {
        dbConfigFile.delete();
      }
      FileUtils.writeStringToFile(dbConfigFile, new Gson().toJson(configuration));
    } catch (Exception e) {
      LOG.error("An error occurs during saving the database configuration.", e);
    }
  }

  public DODatabaseConfiguration loadDatabaseSettings() {
    try {
      if (dbConfigFile.exists()) {
        return new Gson().fromJson(FileUtils.readFileToString(dbConfigFile), DODatabaseConfiguration.class);
      }
    } catch (Exception e) {
      LOG.error("An error occurs during reading the database configuration.", e);
    }
    return null;
  }

}
