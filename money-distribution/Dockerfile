FROM adoptopenjdk/openjdk14:alpine-jre

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
