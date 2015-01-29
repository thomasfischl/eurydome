library OrganisationViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'organisations-view', templateUrl: 'organisations_view.html', useShadowDom: false)
class OrganisationView extends AbstractDOView {

  final RestService restService;
  
  List<Service> services;
  
  OrganisationView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getOrganisations();
    services = restService.getServices();
  }

  @override
  void create() {
    selObject = restService.createOrganisation();
    showDetailPage = true;
  }

  @override
  void save() {
    restService.saveOrganisation(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getOrganisationById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getOrganisationById(id);
    if (obj != null) {
      restService.deleteOrganisation(obj);
    }
    refresh();
  }
}
