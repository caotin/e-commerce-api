# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
# Use Maven image to build the application
WORKDIR /app
# Set the working directory inside the container
COPY . .
# Copy all project files into the container
RUN chmod +x mvnw
# Make the Maven wrapper script executable
RUN ./mvnw clean package -DskipTests
# Build the application and skip tests

# Run stage
FROM eclipse-temurin:21-jre-jammy
# Use a lightweight JRE image to run the application
WORKDIR /app
# Set the working directory inside the container
COPY --from=build /app/target/*.jar app.jar
# Copy the built JAR file from the build stage
EXPOSE 8080
# Expose port 8080 for the application
ENTRYPOINT ["java","-jar","app.jar"]
# Define the entry point to run the application
