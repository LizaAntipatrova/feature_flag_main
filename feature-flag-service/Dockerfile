FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY build/libs/feature-flag-service-0.1-all.jar feature-flag-service.jar
EXPOSE 8080 8084
ENTRYPOINT ["java", "-jar", "feature-flag-service.jar"]
