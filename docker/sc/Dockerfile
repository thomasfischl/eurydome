############################################################
# Dockerfile for SCCaaS
# Based on Ubuntu
############################################################

# Base
FROM dockerfile/java:oracle-java8

#File Author
MAINTAINER Thomas Fischl

############################################################
# Install SCC
############################################################

# Install unzip
RUN apt-get install -y unzip

# Create install folder
RUN mkdir /var/scc

# Copy scc binaries
ADD scc-install.tar /var/scc
ADD start-as.sh /var/scc/start-as.sh
ADD start-fs.sh /var/scc/start-fs.sh
ADD configure-scc.sh /var/scc/configure-scc.sh

############################################################
# Start SCC
############################################################

# Expose SCC-FS ports
EXPOSE 19120

# Start scc services
CMD sh /var/scc/configure-scc.sh && \
	sh /var/scc/start-as.sh & \
	sh /var/scc/start-fs.sh