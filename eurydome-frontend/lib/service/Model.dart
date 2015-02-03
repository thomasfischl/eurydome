library model;

class ApplicationTemplate {
  String id;
  String name;
  String dockerArchive;
  String proxyConfig;
  String healthCheckUrl;

  ApplicationTemplate(this.id, this.name, this.dockerArchive, this.proxyConfig, this.healthCheckUrl);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "dockerArchive": dockerArchive,
    "proxyConfig": proxyConfig,
    "healthCheckUrl": healthCheckUrl
  };

  ApplicationTemplate.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['dockerArchive'], json['proxyConfig'], json['healthCheckUrl']);
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
  String errorMessage;
  String containerId;

  Service(this.id, this.name, this.url, this.applicationRef, this.exposedPort, this.status, this.errorMessage, this.containerId);

  String getFullStatus() {
    if (errorMessage == null) {
      return status;
    } else {
      return "${status} (${errorMessage})";
    }
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "url": url,
    "applicationRef": applicationRef,
    "exposedPort": exposedPort,
    "status": status,
    "errorMessage": errorMessage,
    "containerId": containerId
  };

  Service.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['url'], json['applicationRef'], json['exposedPort'], json['status'], json['errorMessage'], json['containerId']);
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

class ServiceLog {
  String id;
  String name;
  String status;
  String step;
  String totalSteps;
  List<String> logs;

  ServiceLog(this.id, this.name, this.logs, this.status, this.step, this.totalSteps);

  int calculateProgress() {
    if (totalSteps == "0") {
      return 0;
    }
    return (int.parse(step) * 100) ~/ int.parse(totalSteps);
  }

  bool isFinished() {
    return status != "running";
  }

  bool isFailed() {
    return status == "failed";
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "logs": logs,
    "status": status,
    "step": step,
    "totalSteps": totalSteps
  };

  ServiceLog.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['logs'], json['status'], json['step'], json['totalSteps']);
}

class DockerHost {
  String id;
  String name;
  String remoteApiUrl;
  String certificateArchive;
  String containerUrl;

  DockerHost(this.id, this.name, this.remoteApiUrl, this.certificateArchive, this.containerUrl);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "remoteApiUrl": remoteApiUrl,
    "certificateArchive": certificateArchive,
    "containerUrl": containerUrl
  };

  DockerHost.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['remoteApiUrl'], json['certificateArchive'], json['containerUrl']);
}

