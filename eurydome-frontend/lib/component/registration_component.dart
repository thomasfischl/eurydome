library RegistrationComponentLibrary;

import 'package:angular/angular.dart';
import '../service/RestService.dart';
import 'app/viewbase.dart';

@Component(selector: 'registration', templateUrl: 'registration_component.html', useShadowDom: false)
class RegistrationComponent extends AbstractView {

  final RestService restService;

  final Router router;
  
  List<ApplicationTemplate> applications;

  String application;

  String company;

  String user;

  String password;
  
  RegistrationComponent(this.restService,this.router) {
    refresh();
  }

  void refresh() {
    applications = restService.getApplications();
  }

  void register() {
    var registration = restService.registration(user, password, company, application);
    if(registration){
      router.go("login", {});
    } else {
      message.showErrorMessage("Invalid Registration. Please try it again.");
    }
  }

}
