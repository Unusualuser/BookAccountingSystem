FROM tomcat:8-jdk11-corretto
COPY  /target/book-accounting-system.war /usr/local/tomcat/webapps/
EXPOSE 8080