library pmgmt_routing;

import 'package:angular/angular.dart';
import 'dart:html';

void pmgmtRouteInitializer(Router router, RouteViewFactory views) {
  views.configure({
     
    'services': ngRoute(
        path: '/services',
        view: 'view/app_services.html'),
    
    /*
     * Dirty Dirty Hack. This should be improved!!!!! 
     */
        
    'outside-route1' : ngRoute(
        path: '/outsite/:a',
        enter: (RouteEnterEvent e) {
            document.window.location.href = "." + e.path.substring(8);
        }),
      'outside-route2' : ngRoute(
          path: '/outsite/:a/:b',
          enter: (RouteEnterEvent e) {
              document.window.location.href = "." + e.path.substring(8);
          }) ,
      'outside-route3' : ngRoute(
          path: '/outsite/:a/:b/:c',
          enter: (RouteEnterEvent e) {
              document.window.location.href = "." + e.path.substring(8);
          }) 
  });
}