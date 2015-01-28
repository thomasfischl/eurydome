############################################################
# Dockerfile for eurydome demoapp
# Based on Ubuntu
############################################################

# Base
FROM dockerfile/java:oracle-java8

#File Author
MAINTAINER Thomas Fischl

############################################################
# Install Java App
############################################################

RUN mkdir /var/eurydome

# Copy a configuration file from the current directory
ADD eurydome-demoapp.jar /var/eurydome/eurydome-demoapp.jar

############################################################
# Start Apache and Tomcat server
############################################################

# Expose ports
EXPOSE 8080

CMD java -jar /var/eurydome/eurydome-demoapp.jar