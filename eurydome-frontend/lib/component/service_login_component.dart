library ServiceLoginComponentLibrary;

import 'package:angular/angular.dart';
import '../service/RestService.dart';

import 'dart:html';

@Component(selector: 'service-login', templateUrl: 'service_login_component.html', useShadowDom: false)
class ServiceLoginComponent {

  final RestService restService;

  List<Service> services = new List();

  String service;

  String company;

  String user;

  ServiceLoginComponent(this.restService) {
    refresh();
  }

  void refresh() {
    try {
      services.clear();
      if (company != null && user != null) {
        var organisationObj = restService.getOrganisationByName(company);
        var userObj = restService.getUserByName(user);

        if (organisationObj != null && userObj != null && organisationObj.id == userObj.organisation) {
          restService.getServices().forEach((obj) => organisationObj.services.contains(obj.id) ? services.add(obj) : null);
        }
      }
    } catch (e) {
      //nothing to do
    }
  }

  void navigate() {
    if (service != null) {
      var serviceObj = restService.getServiceById(service);
      if (serviceObj != null) {
        window.location.href = "/${serviceObj.url}/";
      }
    }
  }
}
