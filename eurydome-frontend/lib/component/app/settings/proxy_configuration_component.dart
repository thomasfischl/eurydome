library ProxyConfigurationComponentLibrary;

import '../viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'proxy-configuration', templateUrl: 'proxy_configuration_component.html', useShadowDom: false)
class ProxyConfigurationComponent extends AbstractView {

  final RestService restService;

  bool showText;

  String text;

  int textRows = 10;

  ProxyConfigurationComponent(this.restService) {
    showText = false;
  }

  void refresh() {
    showText = true;
    text = restService.getProxyConfiguration();
    textRows = text.split("\n").length;
  }

  void close() {
    showText = false;
  }

  void saveProxyConfiguraiton() {
    try {
      restService.saveProxyConfiguration();
    } catch (e) {
      showErrorMessage(e.toString());
    }
    refresh();
  }
}
