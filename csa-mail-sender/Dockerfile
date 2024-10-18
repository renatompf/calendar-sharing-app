# Step 1: Use a Maven image to build the application
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Copy the entire project into the Docker container
COPY . /app

RUN mvn -pl csa-mail-sender clean package -DskipTests


FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /app/csa-mail-sender/target/csa-mail-sender.jar ./csa-mail-sender.jar

CMD ["java", "-jar", "./csa-mail-sender.jar"]