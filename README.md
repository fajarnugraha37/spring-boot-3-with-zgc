# Playground

A Java 21 Spring Boot application designed to experiment with the Z Garbage Collector (ZGC). This project provides REST endpoints for stress testing, integrates with Prometheus and Grafana for monitoring, and includes k6 scripts for load testing.

## Purpose

The main goal of this repository is to help observe and analyze the behavior of ZGC under various workloads, including constant cache hits, long-running batch processes, and rare memory bursts.

## Features

- REST API endpoints for health checks and stress scenarios
- Prometheus metrics integration via Micrometer
- Example Grafana dashboards ([dashboard/19004_rev1.json](dashboard/19004_rev1.json), [dashboard/4701_rev10.json](dashboard/4701_rev10.json))
- Load testing scripts ([k6/constant-hit.js](k6/constant-hit.js), [k6/long-running.js](k6/long-running.js), [k6/rare-burst.js](k6/rare-burst.js))
- Docker support (`Dockerfile`, `docker-compose.yml`)
- ZGC enabled by default in Docker image

## Getting Started

### Prerequisites

- Java 21
- Docker (optional, for containerization)
- Gradle

### Build & Run

```sh
./gradlew build
./gradlew bootRun
```

Or run with Docker:

```sh
docker build -t playground .
docker-compose up
```

## REST Endpoints

| Method | Path            | Description                                                                 | Parameters                       |
|--------|-----------------|-----------------------------------------------------------------------------|----------------------------------|
| GET    | `/health`       | Health check endpoint                                                       |                                  |
| GET    | `/constant-hit` | Simulates a cache hit and IO operation                                      | `key` (optional, default: "key") |
| GET    | `/long-running` | Simulates a long-running batch process with heavy IO                        | `batchSize` (optional, default: 50) |
| GET    | `/rare-burst`   | Simulates a rare burst with large memory allocation and batch IO            | `mb` (optional, default: 100), `batchSize` (optional, default: 20) |

## Monitoring

- Prometheus metrics exposed at `/actuator/prometheus`
- Example Prometheus config: [prometheus.yml](prometheus.yml)
- Import dashboards from [dashboard/](dashboard/) into Grafana

### Load Testing

Use [k6](https://k6.io/) scripts in [k6/](k6/) for load testing.


#### Available k6 Scripts

- [`k6/constant-hit.js`](k6/constant-hit.js): Simulates a constant load with ramping virtual users.
- [`k6/long-running.js`](k6/long-running.js): Simulates long-running requests for stress testing.
- [`k6/rare-burst.js`](k6/rare-burst.js): Simulates rare burst traffic patterns.

#### Running k6 Tests

You can run k6 tests using Docker Compose:

```sh
docker compose run k6 run /scripts/constant-hit.js
docker compose run k6 run /scripts/long-running.js
docker compose run k6 run /scripts/rare-burst.js
```

Or, if you have k6 installed locally:

```sh
k6 run k6/constant-hit.js
k6 run k6/long-running.js
k6 run k6/rare-burst.js
```

## Documentation

- [Spring Boot Reference](https://docs.spring.io/spring-boot/3.5.4/reference/)
- [Gradle Documentation](https://docs.gradle.org)
- [Spring Boot Guides](https://spring.io/guides/gs/rest-service/)

## License

This project is licensed under the Apache License 2.0.