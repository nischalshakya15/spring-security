FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/spring-jwt

COPY target/spring-jwt.war ./

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "spring-jwt.war"]