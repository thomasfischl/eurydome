package com.github.thomasfischl.eurydome.uploader;

import java.io.IOException;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;

public class DockerCleanup {

  private String dockerUrl;
  private String dockerCertPath;

  public static void main(String[] args) throws IOException {

    if (args.length < 2) {
      System.out.println("Usage:program <dockerUrl> <dockerCertPath>");
      return;
    }

    DockerCleanup obj = new DockerCleanup();
    obj.dockerUrl = args[0];
    obj.dockerCertPath = args[1];

    obj.execute();
  }

  private void execute() throws IOException {

    DockerClient client = DockerUtil.createClient(dockerUrl, dockerCertPath);

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
