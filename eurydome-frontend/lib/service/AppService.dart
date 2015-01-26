library pmgmt_service;

import 'package:angular/angular.dart';

@Injectable()
class ApplicationService {
  
  int _index = 3;
  
  Set<ApplicationTemplate> templates = new Set();
  
  ApplicationService(){
    templates.add(new ApplicationTemplate(0, "Silk Central", "http://dropbox.com/silkcentral"));
    templates.add(new ApplicationTemplate(1, "Rally", "http://dropbox.com/silkcentral"));
    templates.add(new ApplicationTemplate(2, "Jenkins", "http://dropbox.com/silkcentral"));
  }
  
  ApplicationTemplate createTemplate(){
      return new ApplicationTemplate(_index++);
  }
  
  Iterable<ApplicationTemplate> getTemplates(){
    return templates;
  }
  
  void saveTemplate(ApplicationTemplate template) {
    templates.remove(template);
    templates.add(template);
  }
  
  void deleteTemplate(ApplicationTemplate template) {
    templates.remove(template);
  }
}

class ApplicationTemplate {
  
  int id;
  
  String name;
  
  String location;
  
  ApplicationTemplate(this.id, [this.name, this.location]);
  
  bool operator ==(o) => o is ApplicationTemplate && o.id == id;
  int get hashCode => id.hashCode;
  
  ApplicationTemplate clone(){
    return new ApplicationTemplate(this.id, this.name, this.location);
  }
  
  /*
   *  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "category": category,
    "ingredients": ingredients,
    "directions": directions,
    "rating": rating,
    "imgUrl": imgUrl
  };

  Recipe.fromJson(Map<String, dynamic> json): this(json['id'], json['name'], json['category'],
      json['ingredients'], json['directions'], json['rating'], json['imgUrl']);
   */
  
}