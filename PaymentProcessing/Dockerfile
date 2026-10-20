# Stage 1: Build the application
FROM maven:3.9.6-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
#RUN mvn clean package -DskipTests -X

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/PaymentProcessing-0.0.1-SNAPSHOT.jar PaymentProcessing.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "PaymentProcessing.jar"]