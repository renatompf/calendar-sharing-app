# Step 1: Use a Maven image to build the application
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Copy the entire project into the Docker container
COPY . /app

RUN mvn -pl csa-api clean package -DskipTests


FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /app/csa-api/target/csa-api.jar ./csa-api.jar

CMD ["java", "-jar", "./csa-api.jar"]