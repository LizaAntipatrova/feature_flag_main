FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY build/libs/auth-service-0.1-all.jar auth-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
