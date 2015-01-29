library routing;

import 'package:angular/angular.dart';

void routeInitializer(Router router, RouteViewFactory views) {
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
            viewHtml: '<applications-view></applications-view>'),
          'settings': ngRoute(
            path: '/settings',
            viewHtml: '<settings-view></settings-view>'),
          'services': ngRoute(
            path: '/services',
            viewHtml: '<services-view></services-view>')
        })
  });
}