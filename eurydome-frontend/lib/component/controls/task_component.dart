library TaskComponentLibrary;

import '../../service/RestService.dart';

import 'package:angular/angular.dart';

@Component(selector: 'task', templateUrl: 'task_component.html', useShadowDom: false, visibility: Visibility.LOCAL)
class TaskComponent {

  final RestService restService;

  bool visibleDetails = false;

  Task task;

  List<String> settingKeys;
  
  TaskComponent(this.restService) {
  }

  @NgAttr('id')
  void set setTaskId(String id) {
    task = restService.getTaskById(id);
  }

  String getCssClass() {
    if (task.status == 'finished') {
      return "bg-color-success";
    } else if (task.status == 'failed') {
      return "bg-color-failed";
    }
    return "";
  }

  String getTimeSinceExecuted() {
    var difference = new DateTime.now().difference(task.createdAt);
    if (difference.inDays > 0) {
      return "${difference.inDays} days";
    } else if (difference.inHours > 0) {
      return "${difference.inHours} hours";
    } else if (difference.inMinutes > 0) {
      return "${difference.inMinutes} minutes";
    } else {
      return "< 1 minute";
    }
  }

  void showDetails() {
    visibleDetails = !visibleDetails;
  }
  
  List<String> getSettingKeys(){
    if(settingKeys==null){
      settingKeys = task.settings.keys.toList();
    }
    return settingKeys;
  }
}
