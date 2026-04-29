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

## Implemented Features

### Task 1: Get a specific order by ID
- **Endpoint**: `GET /order/{id}`
- **Functionality**: Retrieves a single order by its ID, returning an `OrderDTO` with associated products.
- **Error Handling**: Returns 404 if order not found.

### Task 2: Find customers by name substring
- **Endpoint**: `GET /customer?name={substring}`
- **Functionality**: Performs case-insensitive substring search across any word in customer names (e.g., "John" matches "John Doe" or "Dr. John Smith").
- **Implementation**: Add custom query method using `Spring Data` to support case-insensitive substring search.

### Task 3: Performance Optimisation
- **Issue Identified**: N+1 query problem in GET endpoints due to lazy loading.
- **Solution**: Implemented custom repository methods using `@EntityGraph` to ensures related entities are joined in the initial SQL query, so it doesn't execute a separate query for every entity in the list.
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
- Fixed "Duplicate Key" errors when creating a new entity by updating sequences to the max(id)

### Additional Improvements
- **Service Layer**: Separated business logic into service classes
- **Pagination**: Implemented pageable responses for large datasets
- **Error Handling**: Global exception handler with meaningful error responses
- **Validation**: Added input validation with Bean Validation annotations
- **Testing**: Added comprehensive unit tests for all new endpoints and optimizations
- **OpenAPI**: Updated specification with new endpoints and fixed typos
  - Support automatic generation of OpenApi docs
  - Using annotations to document APIs makes api docs to be easy to maintain

## Assumptions & Decisions
- **Name Search**: Case-insensitive substring matching within words, prioritizing flexibility over strict word boundaries.
- **Products Relationship**: Many-to-many, as products can be shared across orders (inferred from requirement to return order IDs per product).
- **Performance**: Utilised @EntityGraph for precision loading of associations; this avoids the "N+1 select problem" while maintaining LAZY fetching defaults to prevent over-fetching unrelated data.
- **Data Constraints**: No unique constraints on product descriptions to allow flexibility.
- **Deletion**: Products remain in database even if removed from orders; cascade deletion not implemented to preserve data integrity.
- **Pagination**: Default page size of 20; can be overridden via query parameters.

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
4. **Generate OpenApi Docs**:
   ```bash
   ./gradlew generateOpenApiDocs
   ```

## API Endpoints

### Customers
- `GET /customer` - Get all customers (paginated)
- `GET /customer?name={substring}` - Search customers by name
- `POST /customer` - Create a new customer

### Orders
- `GET /order` - Get all orders with products (paginated)
- `GET /order/{id}` - Get specific order by ID
- `POST /order` - Create a new order

### Products
- `GET /products` - Get all products with order IDs
- `GET /products/{id}` - Get specific product with order IDs
- `POST /products` - Create a new product

## Testing the API
Use tools like curl, Postman, or the included OpenAPI spec to test endpoints. Sample requests:

```bash
# Create a product
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"description": "Laptop"}'

# Get all products
curl http://localhost:8080/products

# Search customers
curl "http://localhost:8080/customer?name=john"

# Get order with products
curl http://localhost:8080/order/1
```

## CI/CD Pipeline
Implemented GitHub Actions workflow that:
- Builds the application
- Runs all tests
- Generates JaCoCo coverage report
- Builds and pushes Docker image to GitHub Container Registry
- Requires 80% test coverage to pass

## Performance Considerations
- Optimized queries eliminate N+1 problems
- Database indexes on searchable fields
- Pagination prevents large result sets
- Lazy loading used appropriately to avoid over-fetching

## Future Enhancements
- Implement caching for frequently accessed data
- Add authentication and authorization
- Implement soft deletes for data preservation
- Add integration tests with TestContainers
- Implement rate limiting for API endpoints