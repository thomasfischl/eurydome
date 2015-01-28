library ServicesViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'services-view', templateUrl: 'services_view.html', useShadowDom: false)
class ServicesView extends AbstractDOView {

  final RestService restService;

  ServicesView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getServices();
  }

  @override
  void create() {
    selObject = restService.createService();
    showDetailPage = true;
  }

  @override
  void save() {
    restService.saveService(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getServiceById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getServiceById(id);
    if (obj != null) {
      restService.deleteService(obj);
    }
    refresh();
  }
}
