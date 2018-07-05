FROM zenika/alpine-maven:3-jdk8

COPY src src
COPY pom.xml .

RUN mvn package

CMD java -jar target/*
