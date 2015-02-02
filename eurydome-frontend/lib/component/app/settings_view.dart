library SettingsViewLibrary;

import 'viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'settings-view', templateUrl: 'settings_view.html', useShadowDom: false)
class SettingsView extends AbstractView {

  final RestService restService;

  bool databaseConnected;

  DatabaseConfiguration databaseConfig;

  SettingsView(this.restService) {
    refresh();
  }

  void refresh() {
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
}
