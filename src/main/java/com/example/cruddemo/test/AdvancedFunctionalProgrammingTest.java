package com.example.cruddemo.test;

import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import com.example.cruddemo.test.AdvancedEmployeeFunctionalProgramming;
import com.example.cruddemo.util.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Configuration
public class AdvancedFunctionalProgrammingTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdvancedEmployeeFunctionalProgramming advancedFunctionalProgramming;

    @Bean
    public CommandLineRunner testAdvancedFunctionalProgramming() {
        return args -> {
            // Prepare test data if needed
            prepareTestData();

            // Run and log each functional programming demonstration
            demonstrateSeniorHighPerformers();
            demonstrateEmployeeGrouping();
            demonstrateSalaryAdjustment();
            demonstrateEmployeeEvaluation();
        };
    }

    private void prepareTestData() {
        // Create some sample employees if the repository is empty
        if (employeeRepository.count() == 0) {
            List<Employee> sampleEmployees = List.of(
                new Employee(null, "John", "Doe", "john.doe@example.com", 
                    "Senior Developer", 95000.0, "1234567890"),
                new Employee(null, "Jane", "Smith", "jane.smith@example.com", 
                    "Junior Developer", 65000.0, "0987654321"),
                new Employee(null, "Mike", "Johnson", "mike.johnson@example.com", 
                    "Senior Manager", 120000.0, "5555555555"),
                new Employee(null, "Emily", "Brown", "emily.brown@example.com", 
                    "Junior Analyst", 55000.0, "6666666666")
            );
            employeeRepository.saveAll(sampleEmployees);
        }
    }

    private void demonstrateSeniorHighPerformers() {
        AppLogger.log1Info("--- Demonstrating Senior High Performers ---");
        List<String> seniorHighPerformers = advancedFunctionalProgramming.processEmployeesWithAdvancedFiltering(
            emp -> emp.getPosition().contains("Senior"),
            emp -> emp.getSalary() > 100000,
            Employee::getFullName
        );

        AppLogger.log1Info("Senior High Performers:");
        seniorHighPerformers.forEach(AppLogger::log1Info);
    }

    private void demonstrateEmployeeGrouping() {
        AppLogger.log1Info("\n--- Demonstrating Employee Grouping ---");
        Map<String, List<Employee>> groupedEmployees = advancedFunctionalProgramming.groupEmployeesByComplexCriteria(
            emp -> emp.getPosition() + "_" + (emp.getSalary() > 80000 ? "Senior" : "Junior"),
            emp -> emp.getFirstName().length() > 3
        );

        AppLogger.log1Info("Grouped Employees:");
        groupedEmployees.forEach((key, employees) -> {
            AppLogger.log1Info("Group: " + key);
            employees.forEach(emp -> AppLogger.log1Info(" - " + emp.getFullName()));
        });
    }

    private void demonstrateSalaryAdjustment() {
        AppLogger.log1Info("\n--- Demonstrating Salary Adjustment ---");
        List<Employee> adjustedEmployees = advancedFunctionalProgramming.applyConditionalSalaryAdjustment(
            emp -> emp.getPosition().contains("Junior") && emp.getSalary() < 70000,
            emp -> emp.getSalary() * 1.15  // 15% raise for junior employees
        );

        AppLogger.log1Info("Adjusted Employees:");
        adjustedEmployees.stream()
            .filter(emp -> emp.getPosition().contains("Junior"))
            .forEach(emp -> AppLogger.log1Info(String.format(
                "Employee: %s, Old Salary: %.2f, New Salary: %.2f", 
                emp.getFullName(), 
                emp.getSalary() / 1.15, 
                emp.getSalary()
            )));
    }

    private void demonstrateEmployeeEvaluation() {
        AppLogger.log1Info("\n--- Demonstrating Employee Evaluation ---");
        List<String> evaluatedEmployees = advancedFunctionalProgramming.evaluateEmployeesWithComplexRules(
            emp -> emp.getPosition().contains("Senior"),
            emp -> new BigDecimal(emp.getSalary()).compareTo(BigDecimal.valueOf(90000)) > 0,
            emp -> String.format("Employee: %s, Salary: %.2f, Position: %s", 
                emp.getFullName(), emp.getSalary(), emp.getPosition())
        );

        AppLogger.log1Info("Evaluated Employees:");
        evaluatedEmployees.forEach(AppLogger::log1Info);
    }
}