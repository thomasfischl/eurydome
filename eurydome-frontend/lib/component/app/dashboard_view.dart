library DashboardViewLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'viewbase.dart';

@Component(selector: 'dashboard-view', templateUrl: 'dashboard_view.html', useShadowDom: false)
class DashboardView extends AbstractView {

  final RestService restService;

  List<DockerHost> dockerHosts;

  DashboardView(this.restService) {
    refresh();
    schedulePeriodicTask(new Duration(seconds: 2), () => refresh());
  }

  void refresh() {
    dockerHosts = restService.getDockerHosts();
    List<Service> services = restService.getServices();

    List<Service> unassignedServices = new List();

    outer: for (var service in services) {
      for (var dockerHost in dockerHosts) {
        if (dockerHost.id == service.actualDockerHost || dockerHost.id == service.preferDockerHost) {
          dockerHost.services.add(service);
          continue outer;
        }
      }
      unassignedServices.add(service);
    }

    dockerHosts.add(new DockerHost("-1", "Unassigned", "", "", "")..services = unassignedServices);
  }

  void testDockerHost(String id) {
    var obj = restService.getDockerHostById(id);
    if (obj != null) {
      String resp = restService.testDockerHostConnection(obj.id);
      if (resp == 'OK') {
        message.showSuccessMessage("Connection to docker host '${obj.name}' works!");
      } else {
        message.showErrorMessage("Connection to docker host '${obj.name}' fails! ${resp}");
      }
    }
  }
}
