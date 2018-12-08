FROM centos:7

# nginx
RUN yum -y install epel-release

# Install wget
RUN yum -y install wget

# Install JDK 8
RUN yum -y install java-1.8.0-openjdk

RUN mkdir /application

WORKDIR /application
ENTRYPOINT chmod +x scripts/start-application.sh; scripts/start-application.sh
