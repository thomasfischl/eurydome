library DashboardViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'dashboard-view', templateUrl: 'dashboard_view.html', useShadowDom: false)
class DashboardView {

  final RestService restService;

  List<DockerHost> dockerHosts;

  DashboardView(this.restService) {
    refresh();
  }

  void refresh() {
    dockerHosts = restService.getDockerHosts();
    dockerHosts.forEach((obj) => obj.services = restService.getServices().where((s) => s.actualDockerHost == obj.id || s.preferDockerHost == obj.id).toList());
  }


}
