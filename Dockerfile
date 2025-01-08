#Stage 1
# initialize build and set base image for first stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /usr/src/app/
COPY pom.xml ./

# copy other files
COPY src src

# use the cache of the docker host
RUN --mount=type=cache,target=/Users/anuj/.m2,rw mvn clean package -DskipTests -Pproduction

#Stage 2
# set base image for second stage
FROM eclipse-temurin:21-alpine

COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]