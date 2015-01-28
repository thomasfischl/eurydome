############################################################
# Dockerfile for eurydome demoapp
# Based on Ubuntu
############################################################

# Base
FROM dockerfile/ubuntu

#File Author
MAINTAINER Thomas Fischl

# Update the repository
RUN apt-get update

# Install necessary tools
RUN apt-get install -y wget dialog net-tools

############################################################
# Install Java
# Oracle Java 8 Dockerfile
############################################################

ADD jre-8u25-linux-x64.tar.gz /tmp

# Define working directory.
WORKDIR /data

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
EXPOSE 80

CMD /tmp/jre1.8.0_25/bin/java -jar /var/eurydome/eurydome-demoapp.jar