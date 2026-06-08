FROM maven:3.9.4-eclipse-temurin-21 as builder

WORKDIR /build

# Copy only what is necessary to build (improves cache usage)
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

# Build the project (skip tests for faster image build; change if you want tests run)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jdk

WORKDIR /app

# Install netcat for runtime health checks (kept minimal)
RUN apt-get update \
  && apt-get install -y --no-install-recommends netcat-openbsd \
  && rm -rf /var/lib/apt/lists/*

# Copy the fat JAR produced by the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Copy entrypoint that waits for DB availability
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]
