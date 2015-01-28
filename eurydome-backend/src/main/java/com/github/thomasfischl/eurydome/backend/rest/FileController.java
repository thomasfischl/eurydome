package com.github.thomasfischl.eurydome.backend.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64InputStream;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.thomasfischl.eurydome.backend.dal.MongoDbDataStore;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

@RestController
@RequestMapping(value = "/rest/file")
public class FileController {

  @Inject
  MongoDbDataStore store;

  @RequestMapping(method = RequestMethod.POST, value = "/upload/:id")
  public void remove(@RequestParam("id") String id) {
    if (id != null) {
      GridFS fs = store.getGridFs();
      fs.remove(new ObjectId(id));
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/upload")
  public void upload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    GridFS fs = store.getGridFs();

    try (ServletInputStream is = req.getInputStream()) {
      byte[] buffer = new byte[20];
      is.read(buffer, 0, 5);
      if ("data:".equals(new String(buffer, 0, 5))) {
        while (is.read() != ';') {
        }
        is.read(buffer, 0, 7);

        GridFSInputFile file = fs.createFile(new Base64InputStream(is));
        try (PrintWriter writer = resp.getWriter()) {
          writer.println(file.getId());
        }
      } else {
        resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Invalid data.");
      }
    }
  }

}
