package com.github.thomasfischl.eurydome.backend.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.github.thomasfischl.eurydome.backend.model.DOService;
import com.github.thomasfischl.eurydome.backend.util.SystemUtil;

@Service
public class ProxyService {

  private File folder;

  private File configurationFile;
  
  public ProxyService() {
    if (SystemUtil.isWindows()) {
      folder = new File("./config");
    } else {
      folder = new File("/etc/apache2/sites-available/");
    }

    folder.mkdirs();
    
    configurationFile = new File(folder, "000-admin.conf");
  }

  public void reloadProxy() {
    SystemUtil.executeCommand("/usr/sbin/service apache2 reload");
  }

  public void updateConfiguration(DockerHostConfiguration config, List<DOService> services) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(configurationFile, false))) {

      bw.write("<VirtualHost *:80>                                                                                                                    \n");

      bw.write("ServerAdmin webmaster@localhost \n");
      bw.write("DocumentRoot /var/www/html \n");

      bw.write("ErrorLog ${APACHE_LOG_DIR}/error.log \n");
      bw.write("CustomLog ${APACHE_LOG_DIR}/access.log combined \n");

      bw.write("<Location /mgmt/ > \n");
      bw.write("  ProxyPass         http://localhost:8080/ \n");
      bw.write("  ProxyPassReverse  http://localhost:8080/ \n");
      bw.write(" </Location> \n");

      for (DOService service : services) {
        if (StringUtils.isEmpty(service.getUrl())) {
          continue;
        }
        
        String subdomain = service.getUrl();
        String url = "http://" + config.getHost() + ":" + service.getExposedPort();

        bw.write("  <Location /" + subdomain + "/>                                                                                                      \n");
        bw.write("    ProxyPass         " + url + "                                                                                                     \n");
        bw.write("    ProxyPassReverse  " + url + "                                                                                                     \n");
        bw.write("                                                                                                                                      \n");
        bw.write("    AddOutputFilterByType SUBSTITUTE text/html                                                                                        \n");
        bw.write("    AddOutputFilterByType SUBSTITUTE application/javascript                                                                           \n");
        bw.write("    SetOutputFilter INFLATE;SUBSTITUTE                                                                                                \n");
        bw.write("    Substitute \"s|/silk/|/" + subdomain + "/silk/|ni\"                                                                               \n");
        bw.write("    Substitute \"s|/silkroot/|/" + subdomain + "/silkroot/|ni\"                                                                       \n");
        bw.write("    Substitute \"s|/" + subdomain + "/silkroot/script/extjs-4.1.3/ext-all.js|" + url + "silkroot/script/extjs-4.1.3/ext-all.js|ni\"   \n");
        bw.write("  </Location>                                                                                                                         \n");
      }
      bw.write("</VirtualHost>                                                                                                                        \n");
    }
  }
  
  public String getConfiguration() throws IOException{
    return FileUtils.readFileToString(configurationFile);
  }
}
