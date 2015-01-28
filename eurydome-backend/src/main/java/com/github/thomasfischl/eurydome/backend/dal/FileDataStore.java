package com.github.thomasfischl.eurydome.backend.dal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class FileDataStore {

  @Inject
  public MongoDbDataStore store;

  public void remove(String id) {
    if (id != null) {
      store.getGridFs().remove(new ObjectId(id));
    }
  }

  public List<DOFile> findAll() {
    List<DOFile> result = new ArrayList<DOFile>();
    DBCursor it = store.getGridFs().getFileList();
    while (it.hasNext()) {
      GridFSDBFile file = (GridFSDBFile) it.next();
      result.add(new DOFile(file.getId().toString(), file.getFilename(), String.valueOf(file.getLength() / 1024)));
    }

    return result;
  }

  public GridFS getGridFs() {
    return store.getGridFs();
  }

}
