# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as test
RUN ["./mvnw", "test"]

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:21-jdk as production
COPY --from=build /app/target/dunca-scraper-*.jar /dunca-scraper.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/dunca-scraper.jar"]