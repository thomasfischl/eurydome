library ViewBaseLibrary;

import '../controls/message_component.dart';

abstract class AbstractView {

  MessageComponent message;

  void setMessage(MessageComponent message) {
    this.message = message;
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
