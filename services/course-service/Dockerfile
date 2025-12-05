
FROM maven:3.6.3-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jdk
WORKDIR /app
COPY --from=build /app/target/course-service-1.0-SNAPSHOT.jar ./course-service.jar
EXPOSE 9090
CMD ["java", "-jar", "course-service.jar"]
