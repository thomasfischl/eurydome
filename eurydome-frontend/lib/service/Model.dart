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
  String key;
  String value;

  Setting(this.id, this.key, this.value);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "key": key,
    "value": value
  };

  Setting.fromJson(Map<String, dynamic> json) : this(json['id'], json['key'], json['value']);
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
