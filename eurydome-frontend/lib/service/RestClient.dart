library restclient;

import 'dart:html';
import 'dart:convert';

class RestClient {

  String save(String domainObject, Object obj) {
    if (obj == null) {
      return null;
    }
    return post(domainObject, "save", data: obj);
  }

  void delete(String domainObject, Object obj) {
    if (obj == null) {
      return null;
    }
    post(domainObject, "delete", data: obj);
  }

  List getAll(String domainObject, dynamic map(dynamic obj)) {
    List list = new JsonDecoder().convert(get(domainObject, "list"));
    return list.map(map).toList(growable: true);
  }

  Map<String, dynamic> getById(String domainObject, String id) {
    return new JsonDecoder().convert(get(domainObject, "find", id: id));
  }

  Map<String, dynamic> getByName(String domainObject, String name) {
    return new JsonDecoder().convert(get(domainObject, "find", name: name));
  }

  Map<String, dynamic> create(String domainObject) {
    return new JsonDecoder().convert(post(domainObject, "create"));
  }

  String get(String domainObject, String method, {String id, String name}) {
    HttpRequest request = new HttpRequest();
    if (id != null) {
      request.open("GET", "./rest/${domainObject}/${method}?id=${id}", async: false);
    } else if (name != null) {
      request.open("GET", "./rest/${domainObject}/${method}?name=${name}", async: false);
    } else {
      request.open("GET", "./rest/${domainObject}/${method}", async: false);
    }
    request.send();

    if (request.status != 200) {
      throw new StateError('Get Request "/${domainObject}/${method}" failed. Response: ${request.responseText}');
    }
    return request.responseText;
  }

  String post(String domainObject, String method, {Object data, String id}) {
    HttpRequest request = new HttpRequest();
    if (id != null) {
      request.open("POST", "./rest/${domainObject}/${method}/${id}", async: false);
    } else {
      request.open("POST", "./rest/${domainObject}/${method}", async: false);
    }
    request.setRequestHeader("Content-Type", "application/json");

    if (data != null) {
      request.send(new JsonEncoder().convert(data));
    } else {
      request.send();
    }
    if (request.status != 200) {
      throw new StateError('Post Request "/${domainObject}/${method}" failed. Response: ${request.responseText}');
    }
    return request.responseText;
  }
}
