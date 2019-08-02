FROM maven:alpine AS builder
MAINTAINER David Perez Cabrera, dperezcabrera@gmail.com

COPY . /app/
RUN mvn dependency:go-offline -f /app/pom.xml -Dhttps.proxyHost=10.51.90.20 -Dhttps.proxyPort=8080\
   && mvn package -f /app/pom.xml -Dhttps.proxyHost=10.51.90.20 -Dhttps.proxyPort=8080\
   && mv /app/target/*.jar /app/target/spring-boot-app.jar

FROM openjdk:8-jre-alpine
COPY --from=builder /app/target/spring-boot-app.jar /app/

CMD ["java", "-jar", "/app/spring-boot-app.jar"]
