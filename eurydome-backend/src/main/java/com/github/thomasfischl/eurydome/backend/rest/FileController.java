package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RestController
@RequestMapping(value = "/rest/file")
public class FileController {

  @Inject
  MongoDbDataStore store;

  @RequestMapping(method = RequestMethod.POST, value = "/remove/{id}")
  public void remove(@PathVariable("id") String id) {
    if (id != null) {
      store.getGridFs().remove(new ObjectId(id));
    }
  }

  @RequestMapping(method = RequestMethod.GET, value = "/list")
  public List<DOFile> list() {
    List<DOFile> result = new ArrayList<DOFile>();
    DBCursor it = store.getGridFs().getFileList();
    while (it.hasNext()) {
      GridFSDBFile file = (GridFSDBFile) it.next();
      result.add(new DOFile(file.getId().toString(), file.getFilename(), String.valueOf(file.getLength() / 1024)));
    }

    return result;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/upload")
  public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse resp) throws IOException {
    if (!file.isEmpty()) {
      try {
        byte[] bytes = file.getBytes();

        GridFS fs = store.getGridFs();
        GridFSInputFile f = fs.createFile(bytes);

        f.setFilename(file.getOriginalFilename());
        f.save();

      } catch (Exception e) {
        e.printStackTrace();
        // return "You failed to upload " + name + " => " + e.getMessage();
      }
    } else {
      // return "You failed to upload " + name + " because the file was empty.";
    }
    resp.sendRedirect("/#/app/settings");
  }

}
