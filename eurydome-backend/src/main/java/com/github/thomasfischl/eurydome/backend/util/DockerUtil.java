package com.github.thomasfischl.eurydome.backend.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;
import com.github.thomasfischl.eurydome.backend.dal.FileDataStore;
import com.github.thomasfischl.eurydome.backend.model.DODockerHost;
import com.github.thomasfischl.eurydome.backend.model.DOFile;
import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.google.common.base.Preconditions;

public class DockerUtil {

  private final static Log LOG = LogFactory.getLog(DockerUtil.class);

  // public static final String CERTIFICATION_PATH = "../buildutils/certificates/zeus";

  public static final String CERTIFICATION_PATH = "../buildutils/certificates/work";

  public static DockerClient createClient(String host) {
    return createClient("https://" + host + ":2376", CERTIFICATION_PATH);
  }

  public static DockerClient createClient(DODockerHost dockerHost, FileDataStore fileStore) {
    if (dockerHost.getCertificateArchive() != null) {
      DOFile file = fileStore.findById(dockerHost.getCertificateArchive());
      if (file == null) {
        throw new IllegalStateException("No file found with id '" + dockerHost.getCertificateArchive() + "'.");
      }

      File tempDir = new File(FileUtils.getTempDirectory(), "eurydome");
      try {
        FileUtils.deleteDirectory(tempDir);
      } catch (IOException e) {
        LOG.warn("An error occurs during deleting temp directory.", e);
      }
      tempDir.mkdirs();
      ZipUtil.extract(tempDir, fileStore.getInputStream(dockerHost.getCertificateArchive()));
      return createClient(dockerHost.getRemoteApiUrl(), tempDir.getAbsolutePath());
    } else {
      return createClient(dockerHost.getRemoteApiUrl(), null);
    }
  }

  private static DockerClient createClient(String uri, String certificationPath) {
    DockerClientConfigBuilder cfg = new DockerClientConfigBuilder();
    cfg.withUri(uri);
    if (certificationPath != null) {
      cfg.withDockerCertPath(certificationPath);
    }
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
      if (repoTags != null) {
        for (String repoTag : repoTags) {
          if (tag.equals(repoTag)) {
            return image;
          }
        }
      }
    }
    return null;
  }

  public static void testConnection(DockerClient client) {
    client.pingCmd().exec();
  }

  public static String normalizeContainerName(DOService service) {
    return service.getName().replaceAll(" ", "_").toLowerCase();
  }
}
