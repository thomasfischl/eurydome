library index_site;

import 'package:angular/angular.dart';
import 'package:angular/application_factory.dart';
import 'package:logging/logging.dart';

import 'package:eurydome_frontend/component/components.dart';
import 'package:eurydome_frontend/service/LoginService.dart';
import 'package:eurydome_frontend/routing/login_router.dart';

class PlatformManager extends Module {
  PlatformManager() {
    //Angular Components
    bind(ServiceLoginComponent);
    
    //Angular Services
    bind(LoginService);
    
    //Routing
    bind(RouteInitializerFn, toValue: pmgmtRouteInitializer);
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