FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp

COPY target/*.jar app.jar

ENV TZ=Europe/Moscow

ENTRYPOINT ["java", "-jar", "app.jar"]

