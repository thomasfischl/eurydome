library ApplicationsViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';
import 'dart:html';

@Component(selector: 'applications-view', templateUrl: 'applications_view.html', useShadowDom: false)
class ApplicationsView extends AbstractDOView {

  List<ApplicationTemplate> templates;

  bool visibleDetailTemplateView = false;

  ApplicationTemplate selTemplate;

  final RestService restService;

  ApplicationsView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getApplications();
  }

  @override
  void create() {
    selObject = restService.createApplication();
    showDetailPage = true;
  }

  @override
  void save() {

    var uploadFile = querySelector('#uploadFile');
    var files = uploadFile.files;

    if (files.length > 0 && files[0] is File) {
      File file = files[0];
      restService.uploadFile(file, (id) {
        selObject.dockerArchive = id;
        restService.saveApplication(selObject);
        showDetailPage = false;
        refresh();
      });
    }else{
      restService.saveApplication(selObject);
      showDetailPage = false;
      refresh();
    }
  }

  @override
  void edit(String id) {
    selObject = restService.getApplicationById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getApplicationById(id);
    if (obj != null) {
      restService.deleteApplication(obj);
    }
    refresh();
  }
}
