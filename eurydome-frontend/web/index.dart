library index_site;

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';
import 'package:logging/logging.dart';

import 'package:eurydome_frontend/component/components.dart';
import 'package:eurydome_frontend/service/RestService.dart';
import 'package:eurydome_frontend/routing/router.dart';

class PlatformManager extends Module {
  PlatformManager() {
    //Components
    bind(ServiceLoginComponent);
    bind(NavigationComponent);
    bind(AdminLoginComponent);
    bind(FilePoolComponent);
    bind(ServiceConsoleComponent);
    bind(ProxyConfigurationComponent);
    bind(ServerLogComponent);
    bind(SystemEnvironmentComponent);

    //Control
    bind(MessageComponent);
    bind(ServiceComponent);
    bind(TaskComponent);

    //Views
    bind(ApplicationsView);
    bind(SettingsView);
    bind(ServicesView);
    bind(OrganisationView);
    bind(UserView);
    bind(DockerHostView);
    bind(DashboardView);
    bind(TaskView);

    //Angular Services
    bind(RestService);
    bind(Routes);

    //Routing
    bind(RouteInitializerFn, toImplementation: Routes);
    bind(NgRoutingUsePushState, toValue: new NgRoutingUsePushState.value(false));
  }
}

void main() {
  Logger.root
      ..level = Level.FINEST
      ..onRecord.listen((r) => print(r.message));

  applicationFactory().addModule(new PlatformManager()).run();
}
