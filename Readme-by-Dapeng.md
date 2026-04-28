# Store Application - Technical Assessment Submission

## Overview
This submission implements a complete Spring Boot-based store application that manages customers, orders, and products. The application addresses all four required tasks from the assessment, optimizes performance, and includes production-ready features like validation, error handling, and comprehensive testing.

## Architecture & Technologies
- **Framework**: Spring Boot 3.4.2
- **Database**: PostgreSQL 16.2 with Liquibase migrations
- **ORM**: JPA/Hibernate with optimized queries
- **Mapping**: MapStruct for efficient DTO conversions
- **Testing**: JUnit 5 with Mockito for unit tests
- **Build**: Gradle with JaCoCo coverage and Spotless formatting
- **API Documentation**: OpenAPI 3.0 specification

## Prerequisites
- Java 17+
- Docker (for PostgreSQL)
- Gradle 8.5+

## Running the Application

1. **Start PostgreSQL**:
   ```bash
   docker run -d \
     --name postgres \
     --restart always \
     -e POSTGRES_USER=admin \
     -e POSTGRES_PASSWORD=admin \
     -e POSTGRES_DB=store \
     -v postgres:/var/lib/postgresql/data \
     -p 5433:5432 \
     postgres:16.2 \
     postgres -c wal_level=logical
   ```

2. **Run the Application**:
   ```bash
   ./gradlew bootRun
   ```

3. **Run Tests**:
   ```bash
   ./gradlew test
   ```
