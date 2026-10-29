# ---------- Stage 1: build the JAR ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only pom.xml first so the dependency layer is cached
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Now copy the sources and build (tests already run in the Jenkins Test stage)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Stage 2: slim runtime ----------
FROM eclipse-temurin:17-jre-alpine
LABEL org.opencontainers.image.title="calculator-devops" \
      org.opencontainers.image.description="Calculator REST API (CSE 816 SPE mini-project)"

# Run as a non-root user
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /workspace/target/calculator.jar app.jar
RUN chown -R app:app /app
USER app

EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
