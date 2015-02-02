library NavigationComponentLibrary;

import 'package:angular/angular.dart';
import 'dart:html';

@Component(selector: 'navigation', templateUrl: 'navigation_component.html', useShadowDom: false)
class NavigationComponent {

  void openSettings() {
    var element = querySelector('#dropdown');
    if (element.classes.contains('open')) {
      element.classes.remove('open');
    } else {
      element.classes.add('open');
    }
  }

}
