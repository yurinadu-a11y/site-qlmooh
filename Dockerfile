FROM maven:3.9.16-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/site-qlmooh-1.0.0.jar app.jar
RUN addgroup -S app && adduser -S app -G app
USER app
ENTRYPOINT ["java", "-jar", "app.jar"]
