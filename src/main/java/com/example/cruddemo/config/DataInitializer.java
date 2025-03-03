package com.example.cruddemo.config;

import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Create sample employees
            Employee emp1 = new Employee();
            emp1.setFirstName("John");
            emp1.setLastName("Doe");
            emp1.setEmail("john.doe@example.com");
            emp1.setPhoneNumber("555-123-4567");
            emp1.setPosition("Software Engineer");
            emp1.setSalary(75000.0);
            employeeRepository.save(emp1);

            Employee emp2 = new Employee();
            emp2.setFirstName("Jane");
            emp2.setLastName("Smith");
            emp2.setEmail("jane.smith@example.com");
            emp2.setPhoneNumber("555-987-6543");
            emp2.setPosition("Project Manager");
            emp2.setSalary(85000.0);
            employeeRepository.save(emp2);

            Employee emp3 = new Employee();
            emp3.setFirstName("Michael");
            emp3.setLastName("Johnson");
            emp3.setEmail("michael.johnson@example.com");
            emp3.setPhoneNumber("555-456-7890");
            emp3.setPosition("Software Engineer");
            emp3.setSalary(72000.0);
            employeeRepository.save(emp3);

            System.out.println("Sample data initialized successfully!");
        };
    }
}
