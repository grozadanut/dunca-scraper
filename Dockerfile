# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk AS runtime-base
WORKDIR /app
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
# Only what's needed to run Playwright CLI
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw exec:java \
      -Dexec.mainClass=com.microsoft.playwright.CLI \
      -Dexec.args="install-deps"
RUN ./mvnw exec:java \
      -Dexec.mainClass=com.microsoft.playwright.CLI \
      -Dexec.args="install chromium"

FROM runtime-base AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw package

FROM runtime-base AS production
COPY --from=build /app/target/dunca-scraper-*.jar /dunca-scraper.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/dunca-scraper.jar"]