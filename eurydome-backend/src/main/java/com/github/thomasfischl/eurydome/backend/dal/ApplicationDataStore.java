package com.github.thomasfischl.eurydome.backend.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOApplication;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Service
public class ApplicationDataStore {

  @Inject
  public MongoDbDataStore store;

  private DBCollection collection;

  @PostConstruct
  public void init() {
    collection = store.getCollection("application");
  }

  public DOApplication createObject() {
    DOApplication app = new DOApplication();
    app.setId(UUID.randomUUID().toString());
    collection.insert(app.getDataObject());
    app.validate();
    return app;
  }

  public void save(DOApplication obj) {
    collection.update(new BasicDBObject("id", obj.getId()), obj.getDataObject());
  }

  public void remove(DOApplication obj) {
    collection.remove(obj.getDataObject());
  }

  public List<DOApplication> findAll() {
    List<DOApplication> result = new ArrayList<DOApplication>();

    DBCursor it = collection.find();
    while (it.hasNext()) {
      DOApplication app = new DOApplication((BasicDBObject) it.next());
      app.validate();
      result.add(app);
    }

    return result;
  }

  public void removeAll() {
    collection.drop();
    collection = store.getCollection("application");
  }

}
