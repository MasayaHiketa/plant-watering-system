# 1. build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -f pom.xml clean package -DskipTests

# 2. run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/plant-watering-system-*.jar app.jar
# プロファイルとか必要ならここで ENV
ENV SPRING_PROFILES_ACTIVE=docker
ENV POSTGRES_PASSWORD=Pg18@2025
#ENV WEBHOOK_WATERING_URL=http://127.0.0.1:8081/webhooks/watering
ENV WEBHOOK_WATERING_URL=https://httpbin.org/post

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
