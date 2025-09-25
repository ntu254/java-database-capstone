# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY app/pom.xml app/pom.xml
RUN mvn -f app/pom.xml -q -e -DskipTests dependency:go-offline
COPY app /workspace/app
RUN mvn -f app/pom.xml -q -DskipTests package

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/app/target/smart-clinic-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]