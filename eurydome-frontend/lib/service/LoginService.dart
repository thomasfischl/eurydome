library pmgmt_service;

import 'package:angular/angular.dart';

@Injectable()
class LoginService {
  
  List<String> getAvailableServices(String company, String user){
    if(company == "test" && user == "thomas"){
      return ["Silk Central", "Rally", "Jenkins"]; 
    }
    return null;
  }
}