# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .

# Cấp quyền thực thi cho mvnw và kiểm tra quyền
RUN chmod +x mvnw && ls -l mvnw

# Chạy lệnh Maven, thêm tùy chọn -U để tải lại phụ thuộc
RUN ./mvnw clean package -DskipTests -U

# Kiểm tra xem file JAR có được tạo ra trong thư mục target không
RUN ls /app/target

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
