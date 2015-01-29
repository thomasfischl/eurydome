library UserViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'users-view', templateUrl: 'users_view.html', useShadowDom: false)
class UserView extends AbstractDOView {

  final RestService restService;
  
  List<Organisation> organisations;
  
  UserView(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getUsers();
    organisations = restService.getOrganisations();
  }

  @override
  void create() {
    selObject = restService.createUser();
    showDetailPage = true;
  }

  @override
  void save() {
    restService.saveUser(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getUserById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
    var obj = restService.getUserById(id);
    if (obj != null) {
      restService.deleteUser(obj);
    }
    refresh();
  }
}
