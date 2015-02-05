library DashboardViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'dashboard-view', templateUrl: 'dashboard_view.html', useShadowDom: false)
class DashboardView {

  final RestService restService;

  DashboardView(this.restService) {
    refresh();
  }
  
  void refresh() {
  }

}
