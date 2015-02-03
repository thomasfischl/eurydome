library SystemEnvironmentComponentLibrary;

import '../viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'system-environment', templateUrl: 'system_environment_component.html', useShadowDom: false)
class SystemEnvironmentComponent extends AbstractView {

  final RestService restService;

  bool showText;

  String text;

  int textRows = 10;

  SystemEnvironmentComponent(this.restService) {
    showText = false;
  }

  void refresh() {
    showText = true;
    text = restService.getSystemEnvironment();
    textRows = text.split("\n").length;
    if (textRows > 20) {
      textRows = 20;
    }
  }

  void close() {
    showText = false;
  }
}
