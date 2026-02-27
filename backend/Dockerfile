#  Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Cache dependency layer separately — only re-built when pom.xml changes
COPY backend/pom.xml .
RUN mvn dependency:go-offline -q

# Copy sources from project root (build context is the repo root)
COPY src ./src
RUN mvn package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8080/books > /dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
