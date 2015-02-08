library ServiceConsoleComponentLibrary;

import 'package:angular/angular.dart';
import 'dart:html';

import 'package:eurydome_frontend/service/RestService.dart';
import '../viewbase.dart';

@Component(selector: 'service-console-component', templateUrl: 'service_console_component.html', useShadowDom: false)
class ServiceConsoleComponent extends AbstractView implements ScopeAware {

  final RestService restService;

  Service selService;

  List<String> consoleText;

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
      if (active) {
        schedulePeriodicTask(new Duration(milliseconds: 500), () => updateConsole(true));
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
      Task task;
      try {
        task = restService.getTaskById(selService.currentTask);
      } catch (e) {
        showError(e);
        return;
      }
      consoleText = task.logOutput;
      progress = "${task.calculateProgress()}";
      progressText = "${task.step} of ${task.totalSteps}";

      if (active) {
        scrollToEnd();
      } else {
        showLoadingIndicator = false;
      }

      if (task.isFailed()) {
        statusCssClass = "progress-bar-danger";
      }else{
        statusCssClass = "progress-bar-success";
      }

      if (task.completed) {
        _scope.emit('ServiceStartupComplete');
        scheduleTask(new Duration(milliseconds: 100), () {
          scrollToEnd();
          showLoadingIndicator = false;
        });
      }
    }
  }

  @override
  void set scope(Scope scope) {
    scope.on('startConsole').listen((e) => init(e.data, true));
    scope.on('openConsole').listen((e) => init(e.data, false));
    _scope = scope;
  }

  void close() {
    showConsole = false;
    stopTimer();
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
