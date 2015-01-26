package com.github.thomasfischl.eurydome.backend.mongodb;

import java.io.IOException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDbTest {

  public static void main(String[] args) throws IOException {
    MongoClient mongoClient = new MongoClient("192.168.59.103", 27017);
    DB db = mongoClient.getDB("eurydome");

    System.out.println(db.getStats());

  }

}
