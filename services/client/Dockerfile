# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Root Gradle config
COPY gradlew settings.gradle.kts gradle.properties ./
COPY gradle/wrapper/ gradle/wrapper/
COPY gradle/libs.versions.toml gradle/

# Copy all build files
COPY proto/build.gradle.kts proto/
COPY services/client/build.gradle.kts services/client/

# Download dependencies
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :proto:dependencies :services:client:dependencies --no-daemon || true

# Copy source code
COPY proto/src proto/src
COPY services/client/src services/client/src

# Build distribution
RUN --mount=type=cache,target=/root/.gradle \
    --mount=type=cache,target=/app/build \
    ./gradlew :services:client:installDist --no-daemon --build-cache

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the complete distribution
COPY --from=build /app/services/client/build/install/client/ .

ENTRYPOINT ["./bin/client"]
