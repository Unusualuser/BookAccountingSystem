FROM openjdk:11
COPY /target/book-accounting-system.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080