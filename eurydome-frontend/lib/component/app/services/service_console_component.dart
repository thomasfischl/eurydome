library ServiceConsoleComponentLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'dart:async';
import 'dart:html';

@Component(selector: 'service-console-component', templateUrl: 'service_console_component.html', useShadowDom: false)
class ServiceConsoleComponent implements ScopeAware {

  final RestService restService;

  Service selService;

  List<String> consoleText;

  Timer updateConsoleTimer;

  String progress;

  String progressText;

  String statusCssClass;

  bool showLoadingIndicator = true;

  bool showConsole = false;

  Scope _scope;

  ServiceConsoleComponent(this.restService) {
  }

  void init(String serviceId, bool active) {
    if (serviceId == null) {
      return;
    }
    selService = restService.getServiceById(serviceId);
    if (selService != null) {
      reset();
      stopUpdateTimer();
      if (active) {
        updateConsoleTimer = new Timer.periodic(new Duration(milliseconds: 500), (t) => updateConsole(true));
      } else {
        updateConsole(false);
      }
    }
  }

  void reset() {
    consoleText = null;
    progress = "0";
    statusCssClass = "progress-bar-success";
    showLoadingIndicator = true;
    showConsole = true;
  }

  void refresh() {
    updateConsole(true);
  }

  void updateConsole(bool active) {
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

      if (active) {
        scrollToEnd();
      } else {
        showLoadingIndicator = false;
      }

      if (serviceLog.isFailed()) {
        statusCssClass = "progress-bar-danger";
      }

      if (serviceLog.isFinished()) {
        _scope.emit('ServiceStartupComplete');
        new Timer(new Duration(milliseconds: 100), () {
          scrollToEnd();
          showLoadingIndicator = false;
        });
        stopUpdateTimer();
      }
    }
  }

  @override
  void set scope(Scope scope) {
    scope.on(ScopeEvent.DESTROY).listen((e) => stopUpdateTimer());
    scope.on('startConsole').listen((e) => init(e.data, true));
    scope.on('openConsole').listen((e) => init(e.data, false));
    _scope = scope;
  }

  void close() {
    showConsole = false;
    stopUpdateTimer();
  }

  void stopUpdateTimer() {
    if (updateConsoleTimer != null) {
      updateConsoleTimer.cancel();
      updateConsoleTimer = null;
    }
  }

  void showError(e) {
    consoleText = new List();
    consoleText.add("Error: ${e}");
    statusCssClass = "progress-bar-danger";
    progress = "0";
    progressText = "0 of 0";
    showLoadingIndicator = false;
  }

  void scrollToEnd() {
    var element = querySelector("#loadingIndicator");
    if (element != null) {
      element.scrollIntoView(ScrollAlignment.TOP);
    }
  }
}
