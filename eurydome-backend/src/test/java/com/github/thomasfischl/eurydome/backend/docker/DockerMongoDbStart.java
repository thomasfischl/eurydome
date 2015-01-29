package com.github.thomasfischl.eurydome.backend.docker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;

public class DockerMongoDbStart {

  private static final String CONTAINER_NAME = "MongoDB-Server";

  public static void main(String[] args) throws IOException {

    DockerClient client = DockerUtil.createClient("192.168.59.103");

    Container oldContainer = DockerUtil.getContainerByName(client, CONTAINER_NAME);
    if (oldContainer != null) {
      DockerUtil.deleteContainer(client, oldContainer);
    }
    oldContainer = DockerUtil.getContainerByName(client, CONTAINER_NAME);
    if (oldContainer != null) {
      throw new IllegalStateException("The container '" + CONTAINER_NAME + "' already extis.");
    }

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

    Image image = DockerUtil.getImage(client, "mongodb-server:latest");
    if (image == null) {
      throw new IllegalStateException("No image with tag 'mongodb-server' found.");
    }

    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName(CONTAINER_NAME);
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
    startContainer.withBinds(new Bind("/tmp/mongodb", new Volume("/data/db")));
    startContainer.exec();

    client.close();
  }

}
