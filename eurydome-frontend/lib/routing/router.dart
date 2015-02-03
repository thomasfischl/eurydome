library routing;

import '../service/RestService.dart';
import 'package:angular/angular.dart';
import 'dart:async';

@Injectable()
class Routes
{
  final RestService _restService;
  Router _router;
  
  Routes(this._restService);
  
  void call(Router router, RouteViewFactory views) {
    _router = router;
    
    views.configure({
      'login': ngRoute(
          path: '/login',
          preEnter: checkDatabase(),
          viewHtml: '<service-login></service-login>', 
          defaultRoute: true),
      'adminLogin': ngRoute(
          path: '/adminLogin',
          preEnter: checkDatabase(),
          viewHtml: '<admin-login></admin-login>'),
      'app': ngRoute(
          path: '/app',
          preEnter: checkDatabase(),
          view: 'view/dashboard.html',
          mount: {
            'applications': ngRoute(
              path: '/applications',
              preEnter: checkDatabase(),
              viewHtml: '<applications-view></applications-view>'),
            'settings': ngRoute(
              path: '/settings',
              viewHtml: '<settings-view></settings-view>'),
            'services': ngRoute(
              path: '/services',
              preEnter: checkDatabase(),
              viewHtml: '<services-view></services-view>'),
            'organisations': ngRoute(
              path: '/organisations',
              preEnter: checkDatabase(),
              viewHtml: '<organisations-view></organisations-view>'),
            'users': ngRoute(
              path: '/users',
              preEnter: checkDatabase(),
              viewHtml: '<users-view></users-view>')
          })
    });
  }
  
  Function checkDatabase(){
    return (RoutePreEnterEvent e){
      if(e.route.name == 'app' && e.path == '/settings'){
        return;
      }
      if(!_restService.isDatabaseConnected()){
        e.allowEnter(new Future(() => false));
        _router.go("app.settings", {});
      }
    };
  }
}
