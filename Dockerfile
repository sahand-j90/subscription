FROM openjdk:21-jdk-slim

WORKDIR /

COPY target/subscription*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]