library MessageComponentLibrary;

import '../app/viewbase.dart';

import 'package:angular/angular.dart';

@Component(selector: 'message', templateUrl: 'message_component.html', useShadowDom: false, visibility: Visibility.LOCAL)
class MessageComponent extends AbstractView implements ScopeAware {

  bool showNotification = false;
  String messageText = "";
  String type = "alert-success";

  void _showMessage(String type, String msg) {
    this.type = type;
    messageText = msg;
    showNotification = true;
    stopTimer();
    scheduleTask(new Duration(seconds: 5), () => showNotification=false);
  }

  void showSuccessMessage(String msg) {
    _showMessage("alert-success", msg);
  }

  void showWarningMessage(String msg) {
    _showMessage("alert-warning", msg);
  }

  void showErrorMessage(String msg) {
    _showMessage("alert-danger", msg);
  }

  @override
  void set scope(Scope scope) {
    if (scope.parentScope.context is AbstractView) {
      scope.parentScope.context.setMessage(this);
    }
  }

  void close() {
    stopTimer();
    showNotification = false;
  }
}
