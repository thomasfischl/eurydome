library ApplicationsViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'applications-view', templateUrl: 'applications_view.html', useShadowDom: false)
class ApplicationsView extends AbstractDOView {

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
    restService.saveApplication(selObject);
    showDetailPage = false;
    refresh();
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
