library GenericSettingsLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import '../viewbase.dart';

@Component(selector: 'generic-settings-component', templateUrl: 'generic_settings_component.html', useShadowDom: false)
class GenericSettingsComponent extends AbstractDOView {

  final RestService restService;
  
  GenericSettingsComponent(this.restService) {
    refresh();
  }

  @override
  void refresh() {
    objects = restService.getSettings();
  }

  @override
  void create() {
  }

  @override
  void save() {
    restService.saveSetting(selObject);
    showDetailPage = false;
    refresh();
  }

  @override
  void edit(String id) {
    selObject = restService.getSettingById(id);
    if (selObject != null) {
      showDetailPage = true;
    } else {
      refresh();
    }
  }

  @override
  void delete(String id) {
  }
}
