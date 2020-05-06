FROM maven:alpine AS builder
MAINTAINER David Perez Cabrera, dperezcabrera@gmail.com

COPY . /app/
RUN mvn dependency:go-offline -f /app/pom.xml\
   && mvn package -f /app/pom.xml\
   && mv /app/target/*.jar /app/target/spring-boot-app.jar

FROM openjdk:8-jre-alpine
COPY --from=builder /app/target/spring-boot-app.jar /app/

CMD ["java", "-jar", "/app/spring-boot-app.jar"]
