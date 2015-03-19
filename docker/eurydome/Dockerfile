############################################################
# Dockerfile for apache reverse proxy
# Based on Ubuntu
############################################################

# Base
FROM dockerfile/java:oracle-java8

#File Author
MAINTAINER Thomas Fischl

# Update the repository
RUN apt-get update

############################################################
# Install apache2
############################################################

RUN apt-get install -y apache2 libapache2-mod-proxy-html libxml2-dev
RUN a2enmod proxy           && \
	a2enmod proxy_http      && \
    a2enmod proxy_ajp       && \
    a2enmod rewrite         && \
    a2enmod deflate         && \
    a2enmod headers         && \
    a2enmod proxy_balancer  && \
    a2enmod proxy_connect   && \
    a2enmod proxy_html      && \
    a2enmod substitute      && \
    a2enmod xml2enc

############################################################
# Configure apache2
############################################################

# Copy a configuration file
RUN rm /etc/apache2/sites-enabled/*

ADD 000-admin.conf /etc/apache2/sites-available/
RUN ln -s /etc/apache2/sites-available/000-admin.conf /etc/apache2/sites-enabled/

ADD index.html /var/www/html/

############################################################
# Install Java App
############################################################

RUN mkdir /var/eurydome

# Copy a configuration file from the current directory
ADD java /var/eurydome

############################################################
# Start Apache and Tomcat server
############################################################

# Expose ports
EXPOSE 80

CMD  service apache2 restart && \
        java -Xmx1024m -jar /var/eurydome/eurydome-backend.jar