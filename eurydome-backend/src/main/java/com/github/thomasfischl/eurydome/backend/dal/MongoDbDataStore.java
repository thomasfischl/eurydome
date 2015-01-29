package com.github.thomasfischl.eurydome.backend.dal;

import com.github.thomasfischl.eurydome.backend.model.DODatabaseConfiguration;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;

public class MongoDbDataStore {

  private MongoClient mongoClient;
  private DB db;
  private DODatabaseConfiguration configuration;

  public void confgiure(DODatabaseConfiguration config) {
    this.configuration = config;

    try {
      MongoClientOptions options = MongoClientOptions.builder().connectTimeout(1000).build();
      mongoClient = new MongoClient(new ServerAddress(config.getHost(), config.getPortAsInt()), options);
      db = mongoClient.getDB("eurydome");
      db.getStats();
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

}
