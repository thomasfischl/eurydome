package com.github.thomasfischl.eurydome.uploader;

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

public class DockerCreateAndStart {

  private String containerName;
  private String dockerUrl;
  private String dockerCertPath;
  private String dockerArchive;
  private String portBinding;
  private String volumeBinding;

  public static void main(String[] args) throws IOException {

    if (args.length < 5) {
      System.out
          .println("Usage:program <dockerUrl> <dockerCertPath> <dockerArchive> <containerName> <port> [volumeBinding]");
      return;
    }

    DockerCreateAndStart obj = new DockerCreateAndStart();
    obj.dockerUrl = args[0];
    obj.dockerCertPath = args[1];
    obj.dockerArchive = args[2];
    obj.containerName = args[3];
    obj.portBinding = args[4];
    if (args.length >= 6) {
      obj.volumeBinding = args[5];
    }

    System.out.println("Docker URL:       " + obj.dockerUrl);
    System.out.println("Docker Cert Path: " + obj.dockerCertPath);
    System.out.println("Docker Archive:   " + obj.dockerArchive);
    System.out.println("Container Name:   " + obj.containerName);
    System.out.println("Container Port:   " + obj.portBinding);
    System.out.println("VolumeBinding:    " + obj.volumeBinding);

    obj.execute();
  }

  private void execute() throws IOException {

    DockerClient client = DockerUtil.createClient(dockerUrl, dockerCertPath);

    //
    // delete old container
    //
    Container oldContainer = DockerUtil.getContainerByName(client, containerName);
    if (oldContainer != null) {
      System.out.println("Delete old container '" + oldContainer.getId() + "'");
      DockerUtil.deleteContainer(client, oldContainer);
    }
    oldContainer = DockerUtil.getContainerByName(client, containerName);
    if (oldContainer != null) {
      throw new IllegalStateException("The container '" + containerName + "' already extis.");
    }

    //
    // build image from docker archive
    //
    System.out.println("Upload docker archive '" + dockerArchive + "'");
    String tag = containerName.toLowerCase();
    InputStream response = client.buildImageCmd(new FileInputStream(dockerArchive)).withTag(tag).withNoCache(false)
        .exec();
    StringWriter logwriter = new StringWriter();
    try {
      LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
      while (itr.hasNext()) {
        String line = itr.next();
        logwriter.write(line);
        System.out.println("Container Build: " + line);
      }
    } finally {
      IOUtils.closeQuietly(response);
    }

    //
    // find new image
    //
    Image image = DockerUtil.getImage(client, tag + ":latest");
    if (image == null) {
      throw new IllegalStateException("No image with tag '" + containerName + "' found.");
    }

    //
    // create container
    //
    System.out.println("Create container '" + containerName + "'");
    CreateContainerCmd container = client.createContainerCmd(image.getId());
    container.withName(containerName);
    CreateContainerResponse containerResp = container.exec();

    String[] warnings = containerResp.getWarnings();
    if (warnings != null) {
      for (String warning : warnings) {
        System.out.println("Create Container Warning: " + warning);
      }
    }

    //
    // start container
    //
    System.out.println("Start container '" + containerName + "'");
    String[] ports = portBinding.split(":");
    int port = Integer.parseInt(ports[0]);
    int exposedPort = Integer.parseInt(ports[1]);

    StartContainerCmd startContainer = client.startContainerCmd(containerResp.getId());
    startContainer.withPortBindings(new PortBinding(Ports.Binding(port), ExposedPort.tcp(exposedPort)));

    if (volumeBinding != null) {
      String[] volumnes = volumeBinding.split(":");
      startContainer.withBinds(new Bind(volumnes[0], new Volume(volumnes[1])));
    }

    startContainer.exec();

    client.close();
  }

}
