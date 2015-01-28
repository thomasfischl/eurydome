library ApplicationServiceLibrary;

import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:angular/angular.dart';

import 'RestClient.dart';
import 'Model.dart';

export 'Model.dart';

@Injectable()
class RestService {

  static final String DO_APPLICATION = "application";
  static final String DO_SETTING = "setting";
  static final String DO_SERVICE = "service";

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

  //------------------------------------------
  // Domain Object: Setting
  //------------------------------------------

  List<Setting> getSettings() {
    return _client.getAll(DO_SETTING, (obj) => new Setting.fromJson(obj));
  }

  void saveSetting(Setting obj) {
    _client.save(DO_SETTING, obj);
  }

  //------------------------------------------
  // Domain Object: File
  //------------------------------------------

  void uploadFile(File file, void onFinish(String id)) {

    final reader = new FileReader();
    reader.readAsDataUrl(file);

    var f = reader.onLoad.listen((e) {
      HttpRequest request = new HttpRequest();
      request.open("POST", "/rest/file/upload", async: false);
      request.send(reader.result);
      onFinish(request.responseText);
    });
  }
}
