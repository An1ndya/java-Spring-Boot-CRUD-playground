# Spring Boot CRUD Application with Hibernate

This is a RESTful CRUD (Create, Read, Update, Delete) application built with Spring Boot and Hibernate ORM for database operations.

## Technologies Used

- Java 11
- Spring Boot 2.7.14
- Spring Data JPA (Hibernate)
- H2 Database (in-memory)
- Maven
- RESTful API

## Project Structure

The project follows a standard layered architecture:

- **Model**: Entity classes that are mapped to database tables
- **Repository**: Interfaces that extend JpaRepository for database operations
- **Service**: Business logic layer
- **Controller**: REST API endpoints
- **Exception**: Custom exception handling

## Running the Application

1. Make sure you have Java 11 and Maven installed
2. Clone this repository
3. Navigate to the project directory
4. Run the application using Maven:
   ```
   mvn spring-boot:run
   ```
5. The application will start on port 8080

## Database

The application uses H2 in-memory database which is initialized with sample data when the application starts.

You can access the H2 console at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## API Endpoints

### Employees

| Method | URL                                     | Description                     |
|--------|------------------------------------------|---------------------------------|
| GET    | /api/v1/employees                        | Get all employees               |
| GET    | /api/v1/employees/{id}                   | Get employee by ID              |
| POST   | /api/v1/employees                        | Create a new employee           |
| PUT    | /api/v1/employees/{id}                   | Update an employee              |
| DELETE | /api/v1/employees/{id}                   | Delete an employee              |
| GET    | /api/v1/employees/lastname/{lastName}    | Get employees by last name      |
| GET    | /api/v1/employees/position/{position}    | Get employees by position       |
| GET    | /api/v1/employees/email/{email}          | Get employee by email           |

## Example API Requests

### Create Employee (POST /api/v1/employees)
```json
{
  "firstName": "Alex",
  "lastName": "Wilson",
  "email": "alex.wilson@example.com",
  "phoneNumber": "555-111-2222",
  "position": "Developer",
  "salary": 70000.0
}
```

### Update Employee (PUT /api/v1/employees/{id})
```json
{
  "firstName": "Alexander",
  "lastName": "Wilson",
  "email": "alex.wilson@example.com",
  "phoneNumber": "555-111-3333",
  "position": "Senior Developer",
  "salary": 85000.0
}
```
