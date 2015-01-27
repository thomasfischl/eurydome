library pmgmt_routing;

import 'package:angular/angular.dart';

void pmgmtRouteInitializer(Router router, RouteViewFactory views) {
  views.configure({
    'login': ngRoute(
        path: '/login',
        viewHtml: '<service-login></service-login>', 
        defaultRoute: true),
    'adminLogin': ngRoute(
        path: '/adminLogin',
        viewHtml: '<admin-login></admin-login>'),
    'app': ngRoute(
        path: '/app',
        view: 'view/dashboard.html',
        mount: {
          'applications': ngRoute(
            path: '/applications',
            viewHtml: '<application-templates></application-templates>')
        })
  });
}