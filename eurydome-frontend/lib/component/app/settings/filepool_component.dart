library FilePoolComponentLibrary;

import 'package:angular/angular.dart';
import 'package:eurydome_frontend/service/RestService.dart';

@Component(
    selector: 'filepool',
    templateUrl: 'filepool_component.html',
    useShadowDom: false)
class FilePoolComponent {
  
  List<FileObject> objects;
  
  bool showDetailPage = false;
  
  final RestService restService;

  FilePoolComponent(this.restService) {
    refresh();
  }

  void refresh() {
    objects = restService.getFiles(); 
    showDetailPage = false;
  }

  void delete(String id) {
    restService.deleteFile(id);
    refresh();
  }
  
  void show(){
    showDetailPage = !showDetailPage;
  }
}
