package com.github.thomasfischl.eurydome.backend.model;

public class DOFile {

  private String id;

  private String name;

  private String size;

  public DOFile(String id, String name, String size) {
    super();
    this.id = id;
    this.name = name;
    this.size = size;
  }

  public DOFile() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

}
