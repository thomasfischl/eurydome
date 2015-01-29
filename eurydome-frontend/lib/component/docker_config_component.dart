library DockerConfigComponentLibrary;

import 'app/viewbase.dart';

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(
    selector: 'docker-config', 
    templateUrl: 'docker_config_component.html', 
    useShadowDom: false)
class DockerConfigComponent extends AbstractView{

  String SETTING_DOCKER_HOST = "DOCKER.HOST";
  String SETTING_DOCKER_CERTS = "DOCKER.CERTS";
  
  final RestService restService;

  Map<String, Setting> settings = new Map();
  
  List<FileObject> files = new List();
  String selFile;
  
  DockerConfigComponent(this.restService) {
    refresh();
  }

  void refresh(){
    settings.clear();
    restService.getSettings().forEach((obj) => settings[obj.key] = obj);
    files = restService.getFiles();
    var tmpFile = files.firstWhere((f) => f.id == settings[SETTING_DOCKER_CERTS].value, orElse: ()=>null);
    if(tmpFile!=null){
      selFile = tmpFile.id;
    }
  }
  
  void saveDocker(){
    restService.saveSetting(settings[SETTING_DOCKER_HOST]);
    settings[SETTING_DOCKER_CERTS].value = selFile;
    restService.saveSetting(settings[SETTING_DOCKER_CERTS]);
    showMessage("Docker Configuration saved!");
  }
  
}
