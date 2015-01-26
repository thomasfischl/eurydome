package com.github.thomasfischl.eurydome.backend.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;

public class DockerUtil {

  public static DockerClient createClient(String host) {
    DockerClientConfigBuilder cfg = new DockerClientConfigBuilder();
    cfg.withUri("https://" + host + ":2376");
    cfg.withDockerCertPath("../buildutils/certificates/work");
    return DockerClientBuilder.getInstance(cfg).build();
  }

}
