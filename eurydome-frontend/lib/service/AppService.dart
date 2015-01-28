library ApplicationServiceLibrary;

import 'package:angular/angular.dart';
import 'dart:html';
import 'dart:convert';

@Injectable()
class ApplicationService {

  int _index = 3;

  ApplicationService() {
  }

  ApplicationTemplate createTemplate() {
    return new ApplicationTemplate.fromJson(new JsonDecoder().convert(_get("create")));
  }

  List<ApplicationTemplate> getTemplates() {
    List list = new JsonDecoder().convert(_get("list"));
    return list.map((obj) => new ApplicationTemplate.fromJson(obj)).toList(growable: true);
  }

  void saveTemplate(ApplicationTemplate template) {
    _post("save", new JsonEncoder().convert(template));
  }

  void deleteTemplate(ApplicationTemplate template) {

  }

  String _get(String method) {
    HttpRequest request = new HttpRequest();
    request.open("GET", "/rest/application/" + method, async: false);
    request.send();

    if (request.status != 200) {
      throw new StateError('rest backend error: ' + request.responseText);
    }
    return request.responseText;
  }

  void _post(String method, String data) {
    HttpRequest request = new HttpRequest();
    request.open("POST", "/rest/application/" + method, async: false);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(data);
    if (request.status != 200) {
      throw new StateError('rest backend error');
    }
  }
}

class ApplicationTemplate {

  String id;

  String name;

  String location = "";

  ApplicationTemplate(this.id, [this.name, this.location]);

  bool operator ==(o) => o is ApplicationTemplate && o.id == id;
  int get hashCode => id.hashCode;

  ApplicationTemplate clone() {
    return new ApplicationTemplate(this.id, this.name, this.location);
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
    "id": id,
    "name": name,
    "location": location
  };

  ApplicationTemplate.fromJson(Map<String, dynamic> json) : this(json['id'], json['name'], json['location']);
}
