library ViewBaseLibrary;

import 'dart:async';

class AbstractView {

  bool showNotification = false;
  String toastMessage = "Saved";

  void showMessage(String msg) {
    toastMessage = msg;
    showNotification = true;
    new Timer(new Duration(milliseconds: 2000), () => showNotification = false);
  }
}

abstract class AbstractDOView {
  List objects;
  dynamic selObject;
  bool showDetailPage;

  void refresh();

  void create();

  void save();

  void discard() {
    selObject = null;
    showDetailPage = false;
    refresh();
  }

  void edit(String id);

  void delete(String id);

}
