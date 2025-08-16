# ---- Stage 1: Build jar với Maven ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy pom trước để cache dependency
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -q -DskipTests dependency:go-offline

# copy source vào và build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -DskipTests clean package

# ---- Stage 2: Runtime JRE nhỏ gọn ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# timezone (tùy chọn)
ENV TZ=Asia/Bangkok

# Tên file jar đầu ra (tránh phụ thuộc version)
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# cấu hình mặc định có thể override bằng -e khi run
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

# Chạy app
ENTRYPOINT ["java","-jar","/app/app.jar"]
