package com.github.thomasfischl.eurydome.backend.docker;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;
import com.google.common.base.Preconditions;

public class DockerUtil {

  // private static final String CERTIFICATION_PATH = "../buildutils/certificates/zeus";

  private static final String CERTIFICATION_PATH = "../buildutils/certificates/work";

  public static DockerClient createClient(String host) {
    DockerClientConfigBuilder cfg = new DockerClientConfigBuilder();
    cfg.withUri("https://" + host + ":2376");
    cfg.withDockerCertPath(CERTIFICATION_PATH);
    return DockerClientBuilder.getInstance(cfg).build();
  }

  public static Container getContainerByName(DockerClient client, String name) {
    Preconditions.checkArgument(name != null);
    Preconditions.checkArgument(client != null);

    name = "/" + name;

    List<Container> containers = client.listContainersCmd().withShowAll(true).exec();
    for (Container container : containers) {
      String[] names = container.getNames();
      if (names != null) {
        for (String temp : names) {
          if (name.equals(temp)) {
            return container;
          }
        }
      }
    }
    return null;
  }

  public static void deleteContainer(DockerClient client, Container container) {
    Preconditions.checkArgument(container != null);
    Preconditions.checkArgument(client != null);

    client.killContainerCmd(container.getId()).exec();
    client.removeContainerCmd(container.getId()).withForce().exec();
  }

  public static Image getImage(DockerClient client, String tag) {
    Preconditions.checkArgument(tag != null);
    Preconditions.checkArgument(client != null);

    List<Image> images = client.listImagesCmd().exec();
    for (Image image : images) {
      String[] repoTags = image.getRepoTags();
      if (repoTags != null && repoTags.length > 0 && tag.equals(repoTags[0])) {
        return image;
      }
    }
    return null;
  }
}
