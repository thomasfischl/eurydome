library NavigationComponentLibrary;

import 'package:angular/angular.dart';
import 'dart:html';
import '../service/RestService.dart';

@Component(selector: 'navigation', templateUrl: 'navigation_component.html', useShadowDom: false)
class NavigationComponent {

  Router _router;
  RestService _restService;
  
  NavigationComponent(this._router, this._restService){
  }
  
  void openSettings() {
    var element = querySelector('#dropdown');
    if (element.classes.contains('open')) {
      element.classes.remove('open');
    } else {
      element.classes.add('open');
    }
  }
  
  void logout(){
    _restService.logout();
    _router.go("adminLogin", {});
  }
}
