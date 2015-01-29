library model;

class ApplicationTemplate {
  String id;
  String name;
  String dockerArchive;
  String proxyConfig;

  ApplicationTemplate(this.id, this.name, this.dockerArchive, this.proxyConfig);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "dockerArchive": dockerArchive,
    "proxyConfig": proxyConfig
  };

  ApplicationTemplate.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['dockerArchive'], json['proxyConfig']);
}

class Setting {

  String id;
  String name;
  String value;

  Setting(this.id, this.name, this.value);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "value": value
  };

  Setting.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['value']);
}

class Service {
  String id;
  String name;
  String url;
  String applicationRef;
  String exposedPort;
  String status;

  Service(this.id, this.name, this.url, this.applicationRef, this.exposedPort, this.status);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "url": url,
    "applicationRef": applicationRef,
    "exposedPort": exposedPort,
    "status": status
  };

  Service.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['url'], json['applicationRef'], json['exposedPort'], json['status']);
}

class FileObject {
  String id;
  String name;
  String size;

  FileObject(this.id, this.name, this.size);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "size": size,
  };

  FileObject.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['size']);
}

class DatabaseConfiguration {
  String host;
  String port;

  DatabaseConfiguration(this.host, this.port);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "host": host,
    "port": port,
  };

  DatabaseConfiguration.fromJson(Map<String, dynamic> json) : this(json['host'], json['port']);
}

class Organisation {
  String id;
  String name;
  List<String> services;

  Organisation(this.id, this.name, this.services);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "services": services,
  };

  Organisation.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['services']);
}

class User {
  String id;
  String name;
  String organisation;

  User(this.id, this.name, this.organisation);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "organisationRef": organisation,
  };

  User.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['organisationRef']);
}
