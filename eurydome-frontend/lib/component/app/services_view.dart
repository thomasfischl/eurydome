library ServicesViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';
import 'package:logging/logging.dart';
import 'dart:async';

import 'dart:html';

@Component(selector: 'services-view', templateUrl: 'services_view.html', useShadowDom: false)
class ServicesView extends AbstractDOView {

  final RestService restService;

  bool showConsole = false;

  Service selService;

  List<String> consoleText;

  List<ApplicationTemplate> applications;

  Timer updateConsoleTimer;

  ServicesView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getServices();
    applications = restService.getApplications();
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
      select(obj.id);
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

  void select(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      Logger.root.fine("Select service ${id}");
      showConsole = true;
      selService = obj;
      updateConsole();

      if (updateConsoleTimer != null) {
        updateConsoleTimer.cancel();
      }
      updateConsoleTimer = new Timer.periodic(new Duration(milliseconds: 1000), (t) => updateConsole());
    }
  }

  void updateConsole() {
    if (selService != null) {
      Logger.root.fine(selService.id);
      var serviceLog = restService.getServiceLogByName(selService.id);
      //Logger.root.fine(serviceLog.logs);
      consoleText = serviceLog.logs;
    }
  }

  void closeConsole() {
    selService = null;
    showConsole = false;

    if (updateConsoleTimer != null) {
      updateConsoleTimer.cancel();
    }
  }
}
