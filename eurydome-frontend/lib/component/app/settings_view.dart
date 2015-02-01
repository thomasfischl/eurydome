library SettingsViewLibrary;

import 'dart:html';
import 'dart:async';
import 'viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'settings-view', templateUrl: 'settings_view.html', useShadowDom: false)
class SettingsView extends AbstractView {

  final RestService restService;

  bool databaseConnected;

  bool proxyConfigurationVisible;

  DatabaseConfiguration databaseConfig;

  SettingsView(this.restService) {
    refresh();
  }

  void refresh() {
    proxyConfigurationVisible = false;
    databaseConfig = restService.getDatabaseConfiguration();
    databaseConnected = restService.isDatabaseConnected();
  }

  void saveDatabaseConfig() {
    try {
      restService.saveDatabaseConfiguration(databaseConfig);
      showSuccessMessage("Database Configuration saved! Database Connected!");
    } catch (exception) {
      showErrorMessage("Failed connecting to Database!");
    }
    refresh();
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
}
