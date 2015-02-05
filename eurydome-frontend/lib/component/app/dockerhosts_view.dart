library DockerHostViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'dockerhosts-view', templateUrl: 'dockerhosts_view.html', useShadowDom: false)
class DockerHostView extends AbstractDOView {

  final RestService restService;

  List<FileObject> files = new List();

  DockerHostView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getDockerHosts();
    files = restService.getFiles();
  }

  @override
  void create() {
    selObject = restService.createDockerHost();
    selObject
        ..remoteApiUrl = "https://<host>:2376"
        ..containerUrl = "http://<host>";
    showDetailPage = true;
  }

  @override
  void save() {
    restService.saveDockerHost(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getDockerHostById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getDockerHostById(id);
    if (obj != null) {
      restService.deleteDockerHost(obj);
    }
    refresh();
  }

  void test(String id) {
    var obj = restService.getDockerHostById(id);
    if (obj != null) {
      String resp = restService.testDockerHostConnection(obj.id);
      if (resp == 'OK') {
        message.showSuccessMessage("Connection to docker host works!");
      } else {
        message.showErrorMessage("Connection to docker host fails! ${resp}");
      }
    }
  }
}
