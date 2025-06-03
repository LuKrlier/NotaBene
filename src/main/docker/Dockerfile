# Étape 1 : build du jar avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# COPY pom.xml .
# COPY package.json .
# COPY package-lock.json .
# COPY src ./src
# COPY src/main/webapp ./src/main/webapp
# COPY sonar-project.properties .
COPY . .
RUN mvn -Pprod -DskipTests clean package

# Étape 2 : image légère avec le JAR
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]