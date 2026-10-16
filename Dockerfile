# ============================================================
# Dockerfile — Multi-stage build cho Campus Hub Backend
# ============================================================

# ---- Stage 1: Builder ----
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

# Copy Gradle wrapper và config trước để cache dependency layer
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

# Tải dependencies (layer này được cache nếu build.gradle không đổi)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

# Build JAR (bỏ qua test vì đã chạy riêng bằng `./gradlew test`)
RUN ./gradlew bootJar --no-daemon -x test

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL org.opencontainers.image.title="Campus Hub Backend"
LABEL org.opencontainers.image.description="Spring Boot backend for Campus Hub - PTIT"
LABEL org.opencontainers.image.version="1.0.0"

# Tạo non-root user để tăng bảo mật
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /workspace/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

# Healthcheck — kiểm tra /actuator/health
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
