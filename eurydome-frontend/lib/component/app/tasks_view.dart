library TaskViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'tasks-view', templateUrl: 'tasks_view.html', useShadowDom: false)
class TaskView {

  final RestService restService;

  bool showAllTasks = false;

  List<Task> tasks;
  
  TaskView(this.restService) {
    refresh();
  }

  void refresh() {
    tasks = restService.getTasks();
    tasks.sort((t1, t2) => t2.createdAt.compareTo(t1.createdAt));

    if (!showAllTasks) {
      tasks = tasks.take(10).toList();
    }
  }

  void showAll() {
    showAllTasks = !showAllTasks;
    refresh();
  }
}
