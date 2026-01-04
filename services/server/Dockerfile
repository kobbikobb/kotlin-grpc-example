# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Root Gradle config
COPY gradlew settings.gradle.kts gradle.properties ./
COPY gradle/wrapper/ gradle/wrapper/
COPY gradle/libs.versions.toml gradle/

# Copy all build files
COPY proto/build.gradle.kts proto/
COPY services/server/build.gradle.kts services/server/

# Download dependencies
RUN ./gradlew :proto:dependencies :services:server:dependencies --no-daemon || true

# Copy source code
COPY proto/src proto/src
COPY services/server/src services/server/src

# Build distribution
RUN ./gradlew :services:server:installDist --no-daemon --build-cache

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the complete distribution
COPY --from=build /app/services/server/build/install/server/ .

EXPOSE 50051

ENTRYPOINT ["./bin/server"]
