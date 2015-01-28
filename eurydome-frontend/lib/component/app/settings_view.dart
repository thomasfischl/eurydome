library SettingsViewLibrary;

import 'viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(
    selector: 'settings-view', 
    templateUrl: 'settings_view.html', 
    useShadowDom: false)
class SettingsView extends AbstractView{

  String SETTING_DOCKER_HOST = "DOCKER.HOST";
  String SETTING_DOCKER_CERTS = "DOCKER.CERTS";
  
  final RestService restService;

  Map<String, Setting> settings = new Map();
  
  SettingsView(this.restService) {
    refresh();
  }

  void refresh(){
    settings.clear();
    restService.getSettings().forEach((obj) => settings[obj.key] = obj);
  }
  
  void saveDocker(){
    restService.saveSetting(settings[SETTING_DOCKER_HOST]);
    restService.saveSetting(settings[SETTING_DOCKER_CERTS]);
  }
  
}
