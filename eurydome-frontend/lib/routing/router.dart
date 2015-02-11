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
          preEnter: checkDatabaseAndAuthentication(false),
          viewHtml: '<service-login></service-login>', 
          defaultRoute: true),
      'adminLogin': ngRoute(
          path: '/adminLogin',
          preEnter: checkDatabaseAndAuthentication(false),
          viewHtml: '<admin-login></admin-login>'),
      'registration': ngRoute(
          path: '/registration',
          preEnter: checkDatabaseAndAuthentication(false),
          viewHtml: '<registration></registration>'),
      'app': ngRoute(
          path: '/app',
          preEnter: checkDatabaseAndAuthentication(true),
          view: 'view/dashboard.html',
          mount: {
            'applications': ngRoute(
              path: '/applications',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<applications-view></applications-view>'),
            'settings': ngRoute(
              path: '/settings',
              viewHtml: '<settings-view></settings-view>'),
            'services': ngRoute(
              path: '/services',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<services-view></services-view>'),
            'organisations': ngRoute(
              path: '/organisations',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<organisations-view></organisations-view>'),
            'dockerhosts': ngRoute(
              path: '/dockerhosts',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<dockerhosts-view></dockerhosts-view>'),
            'users': ngRoute(
              path: '/users',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<users-view></users-view>'),
            'tasks': ngRoute(
              path: '/tasks',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<tasks-view></tasks-view>'),
            'dashboard': ngRoute(
              path: '/dashboard',
              preEnter: checkDatabaseAndAuthentication(true),
              viewHtml: '<dashboard-view></dashboard-view>',
              defaultRoute: true)
          })
    });
  }
  
  Function checkDatabaseAndAuthentication(bool needAuth){
    return (RoutePreEnterEvent e){
      if(e.route.name == 'app' && e.path == '/settings'){
        return;
      }
      if(!_restService.isDatabaseConnected()){
        e.allowEnter(new Future(() => false));
        _router.go("app.settings", {});
        return;
      }
      if(needAuth && !_restService.isAuthenticated()){
        print(e.route.toString() + " " + e.path );
        e.allowEnter(new Future(() => false));
        _router.go('adminLogin', {}, replace: true);
        _router.go('adminLogin', {}, replace: false);
        return;
      }
    };
  }
}
