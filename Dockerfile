FROM openjdk:21-jdk-slim

RUN mkdir /ofsset
WORKDIR /offset
RUN touch offset.dat

WORKDIR /

COPY target/subscription*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]