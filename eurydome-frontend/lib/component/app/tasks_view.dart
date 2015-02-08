library TaskViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'tasks-view', templateUrl: 'tasks_view.html', useShadowDom: false)
class TaskView extends AbstractDOView {

  final RestService restService;

  bool showAllTasks = false;

  TaskView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getTasks();
    objects.sort((t1, t2) => t2.createdAt.compareTo(t1.createdAt));

    if (!showAllTasks) {
      objects = objects.take(10).toList();
    }
  }

  @override
  void create() {
  }

  @override
  void delete(String id) {
  }

  @override
  void edit(String id) {
    selObject = restService.getTaskById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void save() {
  }

  String getCssClass(String id) {
    var selObject = restService.getTaskById(id);
    if (selObject != null) {
      if (selObject.status == 'finished') {
        return "success";
      } else if (selObject.status == 'failed') {
        return "danger";
      }
    }
    return "";
  }

  void showAll() {
    showAllTasks = !showAllTasks;
    refresh();
  }
}
