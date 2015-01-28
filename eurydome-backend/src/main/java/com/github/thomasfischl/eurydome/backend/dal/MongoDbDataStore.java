package com.github.thomasfischl.eurydome.backend.dal;

import java.io.IOException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDbDataStore {

  private MongoClient mongoClient;
  private DB db;

  public void init(String host) throws IOException {
    mongoClient = new MongoClient(host, 27017);
    db = mongoClient.getDB("eurydome");
  }

  public DBCollection getCollection(String name) {
    return db.getCollection(name);
  }

}
