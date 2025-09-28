FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

COPY gradle gradle
COPY gradlew gradlew
COPY build.gradle settings.gradle ./
RUN ./gradlew --version
RUN ./gradlew dependencies

COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown appuser:appgroup app.jar
USER appuser

ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:MinRAMPercentage=25.0"

ENV JAVA_OPTS_DEFAULT="-XX:+UseContainerSupport \
-Djava.security.egd=file:/dev/./urandom \
-XX:+ExitOnOutOfMemoryError"

ENV JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 \
-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false \
-Djava.rmi.server.hostname=localhost"

EXPOSE 8080 9010

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --spider -q http://localhost:8080/health || exit 1

ENTRYPOINT exec java $JAVA_OPTS $JAVA_OPTS_DEFAULT $JMX_OPTS -jar app.jar