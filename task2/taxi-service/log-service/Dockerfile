# First stage: JAVA build
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Second stage: Create final image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/log-service-0.0.1-SNAPSHOT.jar log-service-1.0.0.jar
ENTRYPOINT ["java","-jar","log-service-1.0.0.jar"]