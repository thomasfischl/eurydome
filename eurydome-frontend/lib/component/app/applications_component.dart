library ApplicationTemplatesComponentLibrary;

import 'package:angular/angular.dart';
import 'package:logging/logging.dart';
import 'package:eurydome_frontend/service/AppService.dart';

@Component(
    selector: 'application-templates', 
    templateUrl: 'applications_component.html', 
    useShadowDom: false)
class ApplicationsComponent {

  Set<ApplicationTemplate> templates;

  bool visibleDetailTemplateView = false;

  ApplicationTemplate selTemplate;

  final ApplicationService applicationServices;

  ApplicationsComponent(this.applicationServices) {
    loadTemplates();
  }

  void loadTemplates() {
    templates = applicationServices.getTemplates();
  }

  void createNewTemplate() {
    Logger.root.fine("Show template ${visibleDetailTemplateView}");
    visibleDetailTemplateView = !visibleDetailTemplateView;
    if(visibleDetailTemplateView){
      selTemplate = applicationServices.createTemplate();
    } else {
      selTemplate = null;
    }
  }

  void editTemplate(int id) {
    var template = _getTemplate(id);
    if(template!=null){
      selTemplate = template.clone();
      visibleDetailTemplateView = true;
    } else {
      selTemplate = null;
      visibleDetailTemplateView = false;
    }
  }

  void saveTemplate() {
    if (selTemplate != null) {
      applicationServices.saveTemplate(selTemplate);
      selTemplate = null;
      visibleDetailTemplateView = false;
      loadTemplates();
    }
  }

  void deleteTemplate(int id) {
    var temp = _getTemplate(id);
    applicationServices.deleteTemplate(temp);
  }

  ApplicationTemplate _getTemplate(int id) {
    for (var template in templates) {
      if (template.id == id) {
        return template;
      }
    }
    return null;
  }
}
