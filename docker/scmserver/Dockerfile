############################################################
# Dockerfile for eurydome svn server
# Based on Ubuntu
############################################################

# Base
FROM dockerfile/java:oracle-java8

#File Author
MAINTAINER Thomas Fischl

############################################################
# Install Java App
############################################################

RUN mkdir /var/scmserver

ADD scm-server.tar /var/scmserver

############################################################
# Start Apache and Tomcat server
############################################################

# Expose ports
EXPOSE 8080

CMD sh /var/scmserver/bin/scm-server