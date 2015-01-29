package com.github.thomasfischl.eurydome.backend.dal;

import java.io.InputStream;
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
      result.add(convert(file));
    }

    return result;
  }

  public DOFile findById(String id) {
    return convert(store.getGridFs().findOne(new ObjectId(id)));
  }

  public InputStream getInputStream(String id) {
    GridFSDBFile file = store.getGridFs().findOne(new ObjectId(id));
    return file.getInputStream();
  }

  public GridFS getGridFs() {
    return store.getGridFs();
  }

  private DOFile convert(GridFSDBFile file) {
    if (file == null) {
      return null;
    }
    return new DOFile(file.getId().toString(), file.getFilename(), String.valueOf(file.getLength() / 1024));
  }

}
