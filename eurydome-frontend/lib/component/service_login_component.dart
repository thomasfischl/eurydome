library ServiceLoginComponentLibrary;

import 'package:angular/angular.dart';
import '../service/RestService.dart';
import 'app/viewbase.dart';

import 'dart:html';

@Component(selector: 'service-login', templateUrl: 'service_login_component.html', useShadowDom: false)
class ServiceLoginComponent extends AbstractView {

  final RestService restService;

  List<Service> services = new List();

  String service;

  String company;

  String user;

  List<String> consoleText;

  String progress;

  String progressText;

  String statusCssClass;

  bool showConsole = false;

  bool showProgressBar = false;

  Scope _scope;

  int eastereggCount = 0;

  ServiceLoginComponent(this.restService) {
    refresh();
  }

  void refresh() {
    try {
      services.clear();
      if (company != null && user != null) {
        var organisationObj = restService.getOrganisationByName(company);
        var userObj = restService.getUserByName(user);

        if (organisationObj != null && userObj != null && organisationObj.id == userObj.organisation) {
          restService.getServices().forEach((obj) => organisationObj.services.contains(obj.id) ? services.add(obj) : null);
        }
      }
    } catch (e) {
      //nothing to do
    }
  }

  void navigate() {
    if (service != null) {
      var selService = restService.getServiceById(service);
      if (selService != null) {
        if (selService.isActive()) {
          navigateToService(selService);
        } else {

          consoleText = null;
          progress = "0";
          statusCssClass = "progress-bar-success";
          showProgressBar = true;
          showConsole = false;

          stopTimer();
          restService.startService(selService);
          schedulePeriodicTask(new Duration(milliseconds: 500), () => updateConsole(selService));
        }
      }
    }
  }

  void navigateToService(Service service) {
    window.location.href = "/${service.url}/";
  }

  void updateConsole(Service selService) {
    if (selService != null) {
      var serviceLog;
      try {
        serviceLog = restService.getServiceLogByName(selService.id);
      } catch (e) {
        showError(e);
        return;
      }
      consoleText = serviceLog.logs;
      progress = "${serviceLog.calculateProgress()}";
      progressText = "${serviceLog.step} of ${serviceLog.totalSteps}";

      scrollToEnd();

      if (serviceLog.isFailed()) {
        statusCssClass = "progress-bar-danger";
      }

      if (serviceLog.isFinished()) {
        stopTimer();
        navigateToService(selService);
      }
    }
  }

  void activateConsole() {
    showConsole = true;
  }

  void showError(e) {
    consoleText = new List();
    consoleText.add("Error: ${e}");
    statusCssClass = "progress-bar-danger";
    progress = "0";
    progressText = "0 of 0";
  }

  void scrollToEnd() {
    var element = querySelector("#loadingIndicator");
    if (element != null) {
      element.scrollIntoView(ScrollAlignment.TOP);
    }
  }

  void easteregg() {
    eastereggCount++;
    if (eastereggCount == 3) {
      company = "Borland";
      user = "thomas.fischl@borland.com";
      refresh();
      eastereggCount = 0;
    }
  }
}
