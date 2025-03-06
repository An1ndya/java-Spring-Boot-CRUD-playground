package com.example.cruddemo.config;

import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to initialize sample data for the application.
 * This class creates sample employees when the application starts.
 */
@Configuration
@Slf4j
public class DataInitializer {

    // Repository dependency
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor-based dependency injection
     * @param employeeRepository The repository for employee data access
     */
    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        log.info("DataInitializer initialized with repository");
    }

    /**
     * Bean to run initialization logic when the application starts
     * @return CommandLineRunner that creates sample data
     */
    @Bean
    CommandLineRunner initData() {
        return args -> {
            log.info("Starting data initialization...");
            
            // Check if data already exists
            if (employeeRepository.count() > 0) {
                log.info("Data already exists, skipping initialization");
                return;
            }
            
            // Create sample employees
            log.debug("Creating sample employee 1");
            Employee emp1 = new Employee();
            emp1.setFirstName("John");
            emp1.setLastName("Doe");
            emp1.setEmail("john.doe@example.com");
            emp1.setPhoneNumber("555-123-4567");
            emp1.setPosition("Software Engineer");
            emp1.setSalary(75000.0);
            employeeRepository.save(emp1);

            log.debug("Creating sample employee 2");
            Employee emp2 = new Employee();
            emp2.setFirstName("Jane");
            emp2.setLastName("Smith");
            emp2.setEmail("jane.smith@example.com");
            emp2.setPhoneNumber("555-987-6543");
            emp2.setPosition("Project Manager");
            emp2.setSalary(85000.0);
            employeeRepository.save(emp2);

            log.debug("Creating sample employee 3");
            Employee emp3 = new Employee();
            emp3.setFirstName("Michael");
            emp3.setLastName("Johnson");
            emp3.setEmail("michael.johnson@example.com");
            emp3.setPhoneNumber("555-456-7890");
            emp3.setPosition("Software Engineer");
            emp3.setSalary(72000.0);
            employeeRepository.save(emp3);

            log.info("Sample data initialization completed successfully!");
        };
    }
}
