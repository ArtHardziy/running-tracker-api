FROM openjdk:17-jdk-slim-buster
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "-Dspring.profiles.active=dev -Dspring.config.location=classpath:application.yml;file:k8/application-local.yaml"]