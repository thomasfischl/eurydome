library ProxyConfigurationComponentLibrary;

import 'dart:html';
import 'dart:async';
import '../viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'proxy-configuration', templateUrl: 'proxy_configuration_component.html', useShadowDom: false)
class ProxyConfigurationComponent extends AbstractView {

  final RestService restService;

  bool proxyConfigurationVisible;

  ProxyConfigurationComponent(this.restService) {
    refresh();
  }

  void refresh() {
    proxyConfigurationVisible = false;
  }

  void showProxyConfiguraiton() {
    proxyConfigurationVisible = true;

    new Timer(new Duration(milliseconds: 500), () {
      String text = restService.getProxyConfiguration();
      int rows = text.split("\n").length;

      TextAreaElement element = querySelector('#proxyConfiguration');
      element.value = text;
      element.rows = rows;
    });
  }
  
  void saveProxyConfiguraiton(){
    try{
      restService.saveProxyConfiguration();
    }catch(e){
      showErrorMessage(e.toString());
    }
    showProxyConfiguraiton();
  }
}
