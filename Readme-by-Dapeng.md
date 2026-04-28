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

## Implemented Features

### Task 1: Get a specific order by ID
- **Endpoint**: `GET /order/{id}`
- **Functionality**: Retrieves a single order by its ID, returning an `OrderDTO` with associated products.
- **Error Handling**: Returns 404 if order not found.
- **Implementation:**
  - Add new endpoint `GET /order/{id}`
  - Add new unit tests for both the existence and non-existence orders.

### Task 2: Find customers by name substring
- **Endpoint**: `GET /customer?name={substring}`
- **Functionality**: Performs case-insensitive substring search across any word in customer names (e.g., "John" matches "John Doe" or "Dr. John Smith").
- **Implementation**:
  - Add custom query method using `Spring Data` to support case-insensitive substring search.
  - Update `GET /customer` endpoint to support optional `name` query parameter for case-insensitive substring search.
  - Add new unit tests:
    - For successful search
    - For empty results
    - For the query parameter with a blank value

### Task 3: Performance Optimisation
- **Issue Identified**: N+1 query problem in GET endpoints due to lazy loading.
- **Solution**: Implemented custom repository methods using `JOIN FETCH` to load related entities in a single query.
- **Impact**: Reduced database queries from 51 (for 50 customers) to 1 for customer listings.
- **Additional**: Added database index on `customer.name` for faster searches.

### Task 4: Products Management
- **New Entity**: `Product` with ID and description.
- **Relationship**: Many-to-many between Orders and Products (products can appear in multiple orders).
- **Endpoints**:
    - `POST /products` - Create a new product
    - `GET /products` - Retrieve all products with their associated order IDs
    - `GET /products/{id}` - Retrieve a specific product with its order IDs
- **Integration**: Updated order endpoints to include product lists in responses.
- **Database**: Added `product` table and `order_product` join table via Liquibase migration.

### Bug fixed:
- Rename `OrderContollerTests.java` to `OrderControllerTests.java`
- Change`descriptioon` to `description` in `OpenApi.yaml`

## Assumptions & Decisions
- **Name Search**: Case-insensitive substring matching within words, prioritizing flexibility over strict word boundaries.