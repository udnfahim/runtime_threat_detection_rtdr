# Multi-stage build: compile with Maven, produce a lean runtime image
FROM maven:3.9-eclipse-temurin-26 as builder
WORKDIR /workspace
COPY pom.xml mvnw* ./
COPY src ./src
RUN mvn -DskipTests clean package -Dmaven.repo.local=/workspace/.m2/repository

FROM eclipse-temurin:26-jdk-jammy
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseZGC","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
