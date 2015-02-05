library ServicesViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import '../viewbase.dart';
import 'dart:html';

@Component(selector: 'services-view', templateUrl: 'services_view.html', useShadowDom: false)
class ServicesView extends AbstractDOView implements ScopeAware {

  final RestService restService;

  List<ApplicationTemplate> applications;

  List<DockerHost> dockerHosts;

  Scope _scope;
  
  ServicesView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getServices();
    applications = restService.getApplications();
    dockerHosts = restService.getDockerHosts();
  }

  @override
  void create() {
    selObject = restService.createService();
    showDetailPage = true;
  }

  @override
  void save() {
    restService.saveService(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getServiceById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      restService.deleteService(obj);
    }
    refresh();
  }

  void open(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      window.open("/${obj.url}/", obj.name);
    }
    refresh();
  }

  void start(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      restService.startService(obj);
      _scope.broadcast('startConsole', obj.id);
    }
    refresh();
  }

  void stop(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      restService.stopService(obj);
    }
    refresh();
  }

  void showConsole(String id) {
    if (id != null) {
      _scope.broadcast('openConsole', id);
    }
  }

  @override
  void set scope(Scope scope) {
    _scope = scope;
    scope.on('ServiceStartupComplete').listen((e) => refresh());
  }
}
