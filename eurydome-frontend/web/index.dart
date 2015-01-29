library index_site;

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';
import 'package:logging/logging.dart';

import 'package:eurydome_frontend/component/components.dart';

import 'package:eurydome_frontend/service/LoginService.dart';
import 'package:eurydome_frontend/service/RestService.dart';

import 'package:eurydome_frontend/routing/router.dart';

class PlatformManager extends Module {
  PlatformManager() {
    //Components
    bind(ServiceLoginComponent);
    bind(NavigationComponent);
    bind(AdminLoginComponent);
    bind(FilePoolComponent);
    bind(DockerConfigComponent);
    
    //Views
    bind(ApplicationsView);
    bind(SettingsView);
    bind(ServicesView);
    bind(OrganisationView);
    bind(UserView);
    
    //Angular Services
    bind(LoginService);
    bind(RestService);
    
    //Routing
    bind(RouteInitializerFn, toValue: routeInitializer);
    bind(NgRoutingUsePushState, toValue: new NgRoutingUsePushState.value(false));
  }
}

void main() {
  Logger.root..level = Level.FINEST
             ..onRecord.listen((r) => print(r.message));

  applicationFactory()
      .addModule(new PlatformManager())
      .run();
}