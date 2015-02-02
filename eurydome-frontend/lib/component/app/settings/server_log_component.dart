library ServerLogComponentLibrary;

import '../viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'server-log', templateUrl: 'server_log_component.html', useShadowDom: false)
class ServerLogComponent extends AbstractView {

  final RestService restService;

  bool showText;

  String text;

  int textRows = 10;

  ServerLogComponent(this.restService) {
    showText = false;
  }

  void refresh() {
    showText = true;
    text = restService.getServerLog();
    textRows = text.split("\n").length;
  }
}