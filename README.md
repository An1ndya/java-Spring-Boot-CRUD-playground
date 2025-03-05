# Spring Boot CRUD Application with Hibernate

This is a RESTful CRUD (Create, Read, Update, Delete) application built with Spring Boot and Hibernate ORM for database operations.

## Technologies Used

- Java 11
- Spring Boot 2.7.14
- Spring Data JPA (Hibernate)
- MySQL
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

The application uses MySQL database.

### MySQL Setup Instructions

1. **Install MySQL Server**:
   - Download and install MySQL Server from [MySQL official website](https://dev.mysql.com/downloads/mysql/)
   - During installation, set up the root password

2. **Create the Database**:
   - The application is configured to automatically create the database if it doesn't exist
   - The database name is set to `employee_db` in the application.properties file

3. **Configure Database Connection**:
   - The current configuration in `application.properties` is:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3307/employee_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
     spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
     spring.datasource.username=root
     spring.datasource.password=5678
     spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
     ```
   - Update the username, password, and port (if needed) to match your MySQL installation

4. **Hibernate Configuration**:
   - The application uses Hibernate ORM with the following settings:
     ```
     spring.jpa.show-sql=true
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.properties.hibernate.format_sql=true
     ```
   - The `update` mode automatically updates the database schema when entity classes change

5. **Run the Application**:
   - The application will automatically create the necessary tables using Hibernate's schema generation
   - Sample data will be initialized on first run through the `DataInitializer` class

### Troubleshooting MySQL Connection

If you encounter connection issues:

1. Verify MySQL is running:
   ```
   mysql --version
   ```

2. Try connecting to MySQL from command line:
   ```
   mysql -u root -p
   ```

3. Check if you can create and access the database manually:
   ```sql
   CREATE DATABASE IF NOT EXISTS employee_db;
   USE employee_db;
   ```

4. Ensure the credentials and port in application.properties match your MySQL installation

## Logging Configuration

The application implements comprehensive logging with the following configuration:

### Log Levels
- Root level: INFO
- Application package (`com.example.cruddemo`): DEBUG

### Log Output
- Console logging with timestamp, thread, level, logger name, and message
- File logging to `logs/application.log` with the same pattern

### Log Configuration in application.properties
```
logging.level.root=INFO
logging.level.com.example.cruddemo=DEBUG
logging.file.name=logs/application.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Key Logging Points
- Application startup and initialization
- Database operations (through Hibernate SQL logging)
- REST API request handling
- Data initialization process
- Error handling and exceptions

### Accessing Logs
- Console logs are visible in the terminal when running the application
- File logs are stored in the `logs` directory at the root of the project

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

*Last updated: March 5, 2025*
