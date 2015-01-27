library ServiceLoginComponentLibrary;

import 'package:angular/angular.dart';
import 'package:logging/logging.dart';

import 'package:eurydome_frontend/service/LoginService.dart';

@Component(
    selector: 'service-login',
    templateUrl: 'service_login_component.html',
    useShadowDom: false)
class ServiceLoginComponent {
  
  List<String> services;

  String service;
  
  String company;
  
  String user;
  
  final LoginService loginService;
  
  ServiceLoginComponent(this.loginService) {
  }

  void setServices(){
    services = loginService.getAvailableServices(company, user);
    if(services == null){
      service = null;
    }
  }
  
  void login(){
    Logger.root.fine("The user tries to login ${service}");
  }
}