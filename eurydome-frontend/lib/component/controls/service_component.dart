library ServiceComponentLibrary;

import 'package:angular/angular.dart';
import '../../service/RestService.dart';

@Component(selector: 'service-component', templateUrl: 'service_component.html', useShadowDom: false)
class ServiceComponent {

  final RestService restService;

  String serviceId;

  Service service;
  
  String statusCssClass;

  ServiceComponent(this.restService) {
  }

  @NgAttr('id')
  void set setServiceId(String id){
    serviceId = id;
    refresh();
  }
  
  void refresh() {
    service = restService.getServiceById(serviceId);
    
    if(service!=null){
      if(service.status == 'Started' || service.status == 'Starting'){
        statusCssClass = "docker-container-running";
      } else if(service.status == 'Failed'){
        statusCssClass = "docker-container-failed";
      } else {
        statusCssClass = "docker-container-stopped";
      }
    }
  }

}
