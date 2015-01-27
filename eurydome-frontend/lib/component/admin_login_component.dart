library AdminLoginComponentLibrary;

import 'package:angular/angular.dart';
import 'package:logging/logging.dart';

import 'package:eurydome_frontend/service/LoginService.dart';

@Component(
    selector: 'admin-login',
    templateUrl: 'admin_login_component.html',
    useShadowDom: false)
class AdminLoginComponent {
  
  @NgTwoWay("username")
  String username = "admin";
  
  @NgTwoWay("password")
  String password = "admin";
  
  final LoginService loginService;
  
  RouteHandle route;
  
  Router router;
  
  AdminLoginComponent(this.loginService, this.router) {
  }

  void login(){
    Logger.root.fine("The user tries to login ${username} ${password}");
    router.go("app", {});
  }
}