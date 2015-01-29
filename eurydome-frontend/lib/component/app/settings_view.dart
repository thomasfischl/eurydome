library SettingsViewLibrary;

import 'viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(selector: 'settings-view', templateUrl: 'settings_view.html', useShadowDom: false)
class SettingsView extends AbstractView {

  final RestService restService;

  SettingsView(this.restService) {
  }
}