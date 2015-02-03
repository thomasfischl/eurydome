library ViewBaseLibrary;

import 'dart:async';

class AbstractView {

  bool showNotification = false;
  String toastMessage = "Saved";
  String type = "alert-success";

  void showMessage(String msg) {
    toastMessage = msg;
    showNotification = true;
    new Timer(new Duration(milliseconds: 5000), () => showNotification = false);
  }

  void showSuccessMessage(String msg) {
    type = "alert-success";
    showMessage(msg);
  }

  void showWarningMessage(String msg) {
    type = "alert-warning";
    showMessage(msg);
  }

  void showErrorMessage(String msg) {
    type = "alert-danger";
    showMessage(msg);
  }

}

abstract class AbstractDOView extends AbstractView {
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
