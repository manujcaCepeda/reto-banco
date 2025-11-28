# banco-backend/Dockerfile
# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
# copia solo lo necesario para cachear dependencias
COPY src ./src
RUN mvn -B -s /root/.m2/settings.xml -Dmaven.test.skip=true clean package

# Stage 2: runtime
FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=/app/target/*.jar
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
