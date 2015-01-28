package com.github.thomasfischl.eurydome.backend.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.thomasfischl.eurydome.backend.model.AbstractDomainObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class AbstractDataStore<T extends AbstractDomainObject> {

  public MongoDbDataStore store;

  protected DBCollection collection;

  public void init(MongoDbDataStore store) {
    collection = store.getCollection(getCollectionName());
  }

  protected abstract String getCollectionName();

  protected abstract T createEmptyDomainObject();

  protected abstract T createDomainObject(BasicDBObject obj);

  public T createObject() {
    T obj = createEmptyDomainObject();
    obj.setId(UUID.randomUUID().toString());
    collection.insert(obj.getDataObject());
    obj.validate();
    return obj;
  }

  public void save(T obj) {
    collection.update(new BasicDBObject("id", obj.getId()), obj.getDataObject());
  }

  public void remove(T obj) {
    collection.remove(obj.getDataObject());
  }

  public List<T> findAll() {
    List<T> result = new ArrayList<T>();
    DBCursor it = collection.find();
    while (it.hasNext()) {
      result.add(convert(it.next()));
    }
    return result;
  }

  public T findOne(String key, String value) {
    return convert(collection.findOne(new BasicDBObject(key, value)));
  }

  public T findById(String id) {
    return convert(collection.findOne(new BasicDBObject("id", id)));
  }

  public void removeAll() {
    collection.drop();
    collection = store.getCollection(getCollectionName());
  }

  private T convert(DBObject object) {
    if (object == null) {
      return null;
    }

    T doObject = createDomainObject((BasicDBObject) object);
    doObject.validate();
    return doObject;
  }

}
