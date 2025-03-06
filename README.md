# Spring Boot CRUD Application with Minimal Dual Logging

This is a Spring Boot application that demonstrates a CRUD (Create, Read, Update, Delete) functionality for Employee entities with a minimal dual logging system using only Lombok's @Slf4j annotation.

## Features

- RESTful API for Employee management
- MySQL database integration
- Exception handling with custom responses
- Minimal dual logging system using only Lombok's @Slf4j annotation
- Automatic data initialization

## Minimal Dual Logging System

The application uses a minimal dual logging system that allows logging to two separate log files:
- `application.log` - Primary log file
- `secondary.log` - Secondary log file

### How to Use the Minimal Logging System

The logging system is implemented using the `AppLogger` utility class which uses Lombok's `@Slf4j` annotation internally:

```java
// Import the AppLogger utility
import com.example.cruddemo.util.AppLogger;

// In your methods, use the static methods to log to either file
public void yourMethod() {
    // Log to application.log (log1)
    AppLogger.log1Info("This goes to application.log");
    AppLogger.log1Debug("Debug message to application.log");
    AppLogger.log1Warn("Warning message to application.log");
    AppLogger.log1Error("Error message to application.log");
    
    // Log to secondary.log (log2)
    AppLogger.log2Info("This goes to secondary.log");
    AppLogger.log2Debug("Debug message to secondary.log");
    AppLogger.log2Warn("Warning message to secondary.log");
    AppLogger.log2Error("Error message to secondary.log");
    
    // Conditional logging based on a flag
    boolean useSecondLog = true;
    AppLogger.logInfo("This message will go to " + 
        (useSecondLog ? "secondary.log" : "application.log"), useSecondLog);
}
```

### Implementation Details

The `AppLogger` utility class is implemented using Lombok's `@Slf4j` annotation with specific topic parameters:

```java
@Slf4j(topic = "log1")
public class AppLogger {
    // Inner class to access the second logger
    @Slf4j(topic = "log2")
    private static class Log2Holder {
        // This class exists only to hold the log2 logger
    }

    // Static reference to the second logger
    private static final org.slf4j.Logger log2 = Log2Holder.log;
    
    // Static methods to log to each file...
}
```

### Configuration

The logging configuration is defined in `src/main/resources/simple-logging-config.xml`. This file configures the two log files and their respective loggers:

```xml
<!-- Named loggers for each file -->
<logger name="log1" level="INFO" additivity="false">
    <appender-ref ref="FILE_1" />
    <appender-ref ref="CONSOLE" />
</logger>

<logger name="log2" level="INFO" additivity="false">
    <appender-ref ref="FILE_2" />
    <appender-ref ref="CONSOLE" />
</logger>
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven
- MySQL Server

### Running the Application

1. Clone the repository
2. Configure the database connection in `application.properties`
3. Run the application using the PowerShell script:

```
.\start-application.ps1
```

The script will:
1. Check and kill any processes using port 8080
2. Start the application

## API Endpoints

- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get an employee by ID
- `POST /api/employees` - Create a new employee
- `PUT /api/employees/{id}` - Update an employee
- `DELETE /api/employees/{id}` - Delete an employee
