package com.github.thomasfischl.eurydome.backend.docker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;

public class DockerMongoDbStart {

  public static void main(String[] args) throws IOException {

    DockerClient client = DockerUtil.createClient("192.168.59.103");

    InputStream response = client.buildImageCmd(new FileInputStream("../release/docker/mongodb.tar"))
        .withTag("mongodb-server").withNoCache(false).exec();
    StringWriter logwriter = new StringWriter();

    try {
      LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
      while (itr.hasNext()) {
        String line = itr.next();
        logwriter.write(line);
        System.out.println("Line: " + line);
      }
    } finally {
      IOUtils.closeQuietly(response);
    }

    // CreateImageResponse image = client.createImageCmd("", new FileInputStream("../docker/mongodb.tar")).exec();

    List<Image> images = client.listImagesCmd().exec();
    Image image = null;
    for (Image img : images) {
      String[] repoTags = img.getRepoTags();
      if (repoTags != null && repoTags.length > 0 && "mongodb-server:latest".equals(repoTags[0])) {
        image = img;
        System.out.println(image.getId());
      }
    }

    if (image == null) {
      throw new IllegalStateException("No image with tag 'mongodb-server' found.");
    }

    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName("MongoDb-Server");
    container.withCmd("mongod", "--smallfiles");
    CreateContainerResponse containerResp = container.exec();

    String[] warnings = containerResp.getWarnings();
    if (warnings != null) {
      for (String warning : warnings) {
        System.out.println("Warning: " + warning);
      }
    }

    StartContainerCmd startContainer = client.startContainerCmd(containerResp.getId());
    startContainer.withPortBindings(new PortBinding(Ports.Binding(27017), ExposedPort.tcp(27017)));
    // startContainer.withPrivileged(true);
    startContainer.withBinds(new Bind("/tmp/mongodb", new Volume("/data/db")));
    startContainer.exec();

    InspectContainerResponse inspect;
    do {
      System.out.println("Running");
      inspect = client.inspectContainerCmd(containerResp.getId()).exec();

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } while (inspect.getState().isRunning());
    System.out.println("Stopped");
    client.close();
  }
}
