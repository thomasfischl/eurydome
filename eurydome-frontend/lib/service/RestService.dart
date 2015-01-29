library ApplicationServiceLibrary;

import 'dart:convert';

import 'package:angular/angular.dart';

import 'RestClient.dart';
import 'Model.dart';

export 'Model.dart';

@Injectable()
class RestService {

  static final String DO_APPLICATION = "application";
  static final String DO_SETTING = "setting";
  static final String DO_SERVICE = "service";
  static final String DO_FILE = "file";
  static final String DO_DATABASE = "database";

  RestClient _client = new RestClient();

  //------------------------------------------
  // Domain Object: Application
  //------------------------------------------

  ApplicationTemplate createApplication() {
    return new ApplicationTemplate.fromJson(_client.create(DO_APPLICATION));
  }

  ApplicationTemplate getApplicationById(String id) {
    return new ApplicationTemplate.fromJson(_client.getById(DO_APPLICATION, id));
  }

  List<ApplicationTemplate> getApplications() {
    return _client.getAll(DO_APPLICATION, (obj) => new ApplicationTemplate.fromJson(obj));
  }

  void saveApplication(ApplicationTemplate obj) {
    _client.save(DO_APPLICATION, obj);
  }

  void deleteApplication(ApplicationTemplate obj) {
    _client.delete(DO_APPLICATION, obj);
  }

  //------------------------------------------
  // Domain Object: Service
  //------------------------------------------

  Service createService() {
    return new Service.fromJson(_client.create(DO_SERVICE));
  }

  Service getServiceById(String id) {
    return new Service.fromJson(_client.getById(DO_SERVICE, id));
  }

  List<Service> getServices() {
    return _client.getAll(DO_SERVICE, (obj) => new Service.fromJson(obj));
  }

  void saveService(Service obj) {
    _client.save(DO_SERVICE, obj);
  }

  void deleteService(Service obj) {
    _client.delete(DO_SERVICE, obj);
  }

  void startService(Service obj) {
    _client.post(DO_SERVICE, "start", data: obj);
  }

  void stopService(Service obj) {
    _client.post(DO_SERVICE, "stop", data: obj);
  }

  //------------------------------------------
  // Domain Object: Setting
  //------------------------------------------

  List<Setting> getSettings() {
    return _client.getAll(DO_SETTING, (obj) => new Setting.fromJson(obj));
  }

  void saveSetting(Setting obj) {
    _client.save(DO_SETTING, obj);
  }

  String getProxyConfiguration() {
    return _client.get(DO_SETTING, "proxyConfiguration");
  }

  //------------------------------------------
  // Domain Object: File
  //------------------------------------------

  List<FileObject> getFiles() {
    return _client.getAll(DO_FILE, (obj) => new FileObject.fromJson(obj));
  }

  void deleteFile(String id) {
    _client.post(DO_FILE, "remove", id: id);
  }

  //------------------------------------------
  // Domain Object: Database
  //------------------------------------------

  DatabaseConfiguration getDatabaseConfiguration() {
    return new DatabaseConfiguration.fromJson(new JsonDecoder().convert(_client.get(DO_DATABASE, "get")));
  }

  void saveDatabaseConfiguration(DatabaseConfiguration obj) {
    _client.save(DO_DATABASE, obj);
  }

  bool isDatabaseConnected() {
    try {
      getSettings();
      return true;
    } catch (exception, stackTrace) {
      return false;
    }
  }
}
