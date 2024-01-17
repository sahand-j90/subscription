FROM openjdk:21-alpine

WORKDIR /

COPY target/subscription*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]