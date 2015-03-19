#!/bin/bash

cd /var/scc

java \
	-classpath "./lib/tomcat/bootstrap.jar:./lib/tomcat/commons-daemon.jar:./lib/tomcat/tomcat-juli.jar"   \
	-Xmx512m -XX:+UseConcMarkSweepGC -Dcatalina.home="." -Dcatalina.base=Conf/FrontendServer               \
	-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager                                      \
	-Djava.util.logging.config.file=Conf/FrontendServer/conf/logging.properties                            \
	-Djava.rmi.server.useCodebaseOnly=true                                                                 \
	-DsccFrontendBootConf=Conf/FrontendServer/SccFrontendBootConf.xml                                      \
	-Duser.language=en -Dsctm.datadir=./data -DKeepCodeCoverageFiles=false -Djava.net.preferIPv4Stack=true \
	-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false             \
	-Dcom.sun.management.jmxremote.port=19140                                                              \
	org.apache.catalina.startup.Bootstrap start
