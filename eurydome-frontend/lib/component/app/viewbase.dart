library ViewBaseLibrary;

import 'package:angular/angular.dart';
import 'dart:async';

import '../controls/message_component.dart';


abstract class AbstractView implements ScopeAware {

  MessageComponent message;

  Scope _scope;

  Timer _timer;

  void setMessage(MessageComponent message) {
    this.message = message;
  }

  void set scope(Scope scope) {
    _scope = scope;
    _scope.on(ScopeEvent.DESTROY).listen((e) => stopTimer());
  }

  void schedulePeriodicTask(Duration duration, void execute()) {
    stopTimer();
    _timer = new Timer.periodic(duration, (t) => execute());
  }

  void scheduleTask(Duration startTime, void execute()) {
    stopTimer();
    _timer = new Timer(startTime, () => execute());
  }

  void stopTimer() {
    if (_timer != null) {
      _timer.cancel();
      _timer = null;
    }
  }
}

abstract class AbstractDOView extends AbstractView {
  List objects;
  dynamic selObject;
  bool showDetailPage;

  void refresh();

  void create();

  void save();

  void discard() {
    selObject = null;
    showDetailPage = false;
    refresh();
  }

  void edit(String id);

  void delete(String id);
}
