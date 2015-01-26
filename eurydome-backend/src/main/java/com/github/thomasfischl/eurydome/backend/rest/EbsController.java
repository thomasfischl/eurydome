package com.github.thomasfischl.eurydome.backend.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbsController {

  @RequestMapping("/ebs/vmsizes")
  public List<String> getVmSizes() {
    ArrayList<String> result = new ArrayList<String>();
    result.add("t2.micro");
    result.add("t2.small");
    result.add("t2.medium");
    return result;
  }
  
  
  

  // private void todo() {
  // BasicAWSCredentials credentials = new BasicAWSCredentials("", "");
  // AWSElasticBeanstalkAsync client = new AWSElasticBeanstalkAsyncClient(credentials);
  //
  // CreateApplicationRequest req = new CreateApplicationRequest("Test");
  // req.withGeneralProgressListener(new ProgressListener() {
  // @Override
  // public void progressChanged(ProgressEvent e) {
  // }
  // });
  //
  // client.createApplication(req);
  //
  // CreateEnvironmentRequest req1 = new CreateEnvironmentRequest();
  // ArrayList<ConfigurationOptionSetting> optionSettings = new ArrayList<ConfigurationOptionSetting>();
  // req1.setOptionSettings(optionSettings);
  // client.createEnvironment(req1);
  // }

}
