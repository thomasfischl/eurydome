package com.github.thomasfischl.eurydome.backend.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZipUtil {

  private final static Log LOG = LogFactory.getLog(ZipUtil.class);
  
  public static void extract(File folder, InputStream zipStream) {
    byte[] buffer = new byte[1024];

    if (!folder.exists()) {
      folder.mkdir();
    }

    try (ZipInputStream zis = new ZipInputStream(zipStream)) {
      ZipEntry ze = zis.getNextEntry();

      while (ze != null) {
        File newFile = new File(folder, ze.getName());
        LOG.debug("file unzip : " + newFile.getAbsoluteFile());

        new File(newFile.getParent()).mkdirs();

        try (FileOutputStream fos = new FileOutputStream(newFile)) {
          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }
        }
        ze = zis.getNextEntry();
      }

      zis.closeEntry();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
