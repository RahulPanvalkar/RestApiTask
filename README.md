# RestApiTask
This repository contains a CRUD REST API application. The application is built using Java 17 and Spring Boot 3.3.6 and demonstrates best practices for implementing RESTful APIs with proper error handling, validation, and database integration.

## Features
- CRUD Operations: Create, Read, Update, and Delete entities.
- Pagination: Supports pagination with configurable `page` and `size` query parameters (default `page = 0` and `size = 5`).
- Validation: Input validation for request payloads.
- Error Handling: Consistent and structured error responses.
- Environment Variables: Database credentials are managed securely using environment variables.
- Technologies Used: Java 17 | Spring Boot 3.3.6 | Hibernate/JPA | MySQL

## Setup Instructions
- Prerequisites : Java 17 | Maven | MySQL Database | Git
- Environment Variables : Set the following environment variables for database connectivity
  | Variable    | Description                   | Example                       |
  |-------------|-------------------------------|-------------------------------|
  | DB_URL      | JDBC URL for your database    | jdbc:mysql://localhost:3306   |
  | DB_USERNAME | Username for the database     | Username                      |
  | DB_PASSWORD | Password for the database     | Password                      |

## Database Schema
- Category: Stores category information with fields like id, name and description.
- Product: Stores product information with fields like id, name, price, quantity and a foreign key to Category.

## API Endpoints

| HTTP Method | Endpoint                   | Description                       | Request Body                 | Response Body                  |
|-------------|----------------------------|-----------------------------------|------------------------------|--------------------------------|
| GET         | `/api/categories`          | Get all categories  (paginated)   | N/A                          | JSON array of categories       |
| GET         | `/api/categories/{id}`     | Get category by ID                | N/A                          | JSON object of a category      |
| POST        | `/api/categories`          | Add a new category                | Category JSON                | Created Category JSON          |
| PUT         | `/api/categories/{id}`     | Update a category by ID           | Updated category JSON        | Updated category JSON          |
| DELETE      | `/api/categories/{id}`     | Delete a category by ID           | N/A                          | Success message JSON           |
| GET         | `/api/products`            | Get all products  (paginated)     | N/A                          | JSON array of products         |
| GET         | `/api/products/{id}`       | Get product by ID                 | N/A                          | JSON object of a product       |
| POST        | `/api/products`            | Add a new product                 | Product JSON                 | Created product JSON           |
| PUT         | `/api/products/{id}`       | Update a product by ID            | Updated product JSON         | Updated product JSON           |
| DELETE      | `/api/products/{id}`       | Delete a product by ID            | N/A                          | Success message JSON           |


### Example Request Body for POST `/api/categories`:
```json
{
    "categoryName": "Electronics",
    "description": "All electronic products"
}
```

### Example Request Body for POST `/api/products`:
```json
{
  "productName": "Laptop GenZ",
  "category": {
    "categoryId": 1
  },
  "price": 65999,
  "quantity": 10,
  "description": "A high-performance gaming Laptop."
}
```

### Notes:
- Replace `{id}` with the actual ID of the product or category.
- Use proper JSON format for request bodies.
- For paginated endpoints, use query parameters `page` (default: 0) and `size` (default: 5) to control pagination.
- Example: `/api/products?page=1&size=10` retrieves the second page of products with 10 items per page.



## Author
- Rahul Panvalkar
