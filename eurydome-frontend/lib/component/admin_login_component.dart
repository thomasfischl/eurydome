library AdminLoginComponentLibrary;

import 'package:angular/angular.dart';

@Component(selector: 'admin-login', templateUrl: 'admin_login_component.html', useShadowDom: false)
class AdminLoginComponent {

  String username = "admin";

  String password = "admin";

  RouteHandle route;

  Router router;

  AdminLoginComponent(this.router) {
  }

  void login() {
    router.go("app", {});
  }
}
