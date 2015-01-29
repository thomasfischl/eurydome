package com.github.thomasfischl.eurydome.backend.docker;

import java.io.IOException;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.thomasfischl.eurydome.backend.util.DockerUtil;

public class DockerCleanup {

  public static void main(String[] args) throws IOException {

    DockerClient client = DockerUtil.createClient("192.168.59.103");

    List<Container> containers = client.listContainersCmd().withShowAll(true).exec();
    for (Container container : containers) {
      System.out.println(container.getId() + " " + container.getImage() + " " + container.getStatus());

      if (container.getStatus().startsWith("Up")) {
        System.out.println("Stop container " + container.getId());
        client.stopContainerCmd(container.getId()).exec();
      }
      System.out.println("Cleanup container " + container.getId());
      client.removeContainerCmd(container.getId()).withForce(true).exec();
    }

    client.close();
  }
}
