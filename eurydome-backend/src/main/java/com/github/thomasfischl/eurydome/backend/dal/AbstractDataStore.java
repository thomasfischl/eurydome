package com.github.thomasfischl.eurydome.backend.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.github.thomasfischl.eurydome.backend.model.AbstractDomainObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class AbstractDataStore<T extends AbstractDomainObject> {

  @Inject
  public MongoDbDataStore store;

  protected DBCollection getCollection() {
    return store.getCollection(getCollectionName());
  }

  protected abstract String getCollectionName();

  protected abstract T createEmptyDomainObject();

  protected abstract T createDomainObject(BasicDBObject obj);

  public T createObject() {
    T obj = createEmptyDomainObject();
    obj.setId(UUID.randomUUID().toString());
    getCollection().insert(obj.getDataObject());
    obj.validate();
    return obj;
  }

  public void save(T obj) {
    getCollection().update(new BasicDBObject("id", obj.getId()), obj.getDataObject());
  }

  public void remove(T obj) {
    getCollection().remove(obj.getDataObject());
  }

  public List<T> findAll() {
    List<T> result = new ArrayList<T>();
    DBCursor it = getCollection().find();
    while (it.hasNext()) {
      result.add(convert(it.next()));
    }
    return result;
  }

  public T findOne(String key, String value) {
    return convert(getCollection().findOne(new BasicDBObject(key, value)));
  }

  public T findById(String id) {
    return convert(getCollection().findOne(new BasicDBObject("id", id)));
  }

  public T findByName(String name) {
    return findOne("name", name);
  }

  public void removeAll() {
    getCollection().drop();
  }

  private T convert(DBObject object) {
    if (object == null) {
      return null;
    }

    T doObject = createDomainObject((BasicDBObject) object);
    doObject.validate();
    return doObject;
  }

  public boolean isConnected() {
    return store.isConnected();
  }

}
