library pmgmt_routing;

import 'package:angular/angular.dart';

void pmgmtRouteInitializer(Router router, RouteViewFactory views) {
  views.configure({

    'services': ngRoute(
        path: '/services',
        view: 'view/app_services.html'),
    
     /*'login': ngRoute(
        path: '/Login',
        view: 'view/login.html',
        defaultRoute: true),
    'recipe': ngRoute(
        path: '/recipe/:recipeId',
        mount: {
          'view': ngRoute(
              path: '/view',
              view: 'view/viewRecipe.html'),
          'edit': ngRoute(
              path: '/edit',
              view: 'view/editRecipe.html'),
          'view_default': ngRoute(
              defaultRoute: true,
              enter: (RouteEnterEvent e) =>
                  router.go('view', {},
                      startingFrom: router.root.findRoute('recipe'),
                      replace: true))
        })*/
  });
}