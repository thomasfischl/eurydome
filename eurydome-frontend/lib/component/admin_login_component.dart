library AdminLoginComponentLibrary;

import 'package:angular/angular.dart';

import '../service/RestService.dart';
import 'app/viewbase.dart';

@Component(selector: 'admin-login', templateUrl: 'admin_login_component.html', useShadowDom: false)
class AdminLoginComponent extends AbstractView {

  final RestService restService;

  String username = "admin";

  String password = "admin";

  RouteHandle route;

  Router router;

  AdminLoginComponent(this.router, this.restService) {
  }

  void login() {
    if (restService.login(username, password)) {
      router.go("app", {});
    } else {
      message.showErrorMessage("Invalid username or password");
    }
  }
}
