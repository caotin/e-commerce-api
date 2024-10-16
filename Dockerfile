FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ecommerce-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ecommerce-0.0.1-SNAPSHOT.jar"]