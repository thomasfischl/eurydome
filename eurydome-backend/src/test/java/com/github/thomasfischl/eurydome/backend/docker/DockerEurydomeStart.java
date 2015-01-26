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
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

public class DockerEurydomeStart {

  public static void main(String[] args) throws IOException {

    DockerClient client = DockerUtil.createClient("192.168.59.103");

    InputStream response = client.buildImageCmd(new FileInputStream("../release/docker/eurydome.tar"))
        .withTag("eurydome-server").withNoCache(false).exec();
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
      if (repoTags != null && repoTags.length > 0 && "eurydome-server:latest".equals(repoTags[0])) {
        image = img;
        System.out.println(image.getId());
      }
    }

    if (image == null) {
      throw new IllegalStateException("No image with tag 'eurydome-server' found.");
    }

    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName("Eurydome-Server");
    CreateContainerResponse containerResp = container.exec();

    String[] warnings = containerResp.getWarnings();
    if (warnings != null) {
      for (String warning : warnings) {
        System.out.println("Warning: " + warning);
      }
    }

    StartContainerCmd startContainer = client.startContainerCmd(containerResp.getId());
    startContainer.withPortBindings(new PortBinding(Ports.Binding(80), ExposedPort.tcp(80)));
    startContainer.exec();

    for (int i = 0; i < 10; i++) {
      InspectContainerResponse inspect = client.inspectContainerCmd(containerResp.getId()).exec();
      System.out.println("Status: " + inspect.getState().toString());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    client.close();
  }
}
