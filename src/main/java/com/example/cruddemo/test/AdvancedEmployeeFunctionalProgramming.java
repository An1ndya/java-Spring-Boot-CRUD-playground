package com.example.cruddemo.test;

import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Service
public class AdvancedEmployeeFunctionalProgramming {

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. Advanced Functional Interface for Employee Processing
    @FunctionalInterface
    interface EmployeeProcessor<R> {
        R process(List<Employee> employees, 
                  Predicate<Employee> filter, 
                  Function<Employee, R> transformation);
    }

    // 2. Complex Employee Stream Processing Methods
    public List<String> processEmployeesWithAdvancedFiltering(
            Predicate<Employee> skillFilter,
            Predicate<Employee> salaryFilter,
            Function<Employee, String> employeeMapper) {
        
        List<Employee> allEmployees = employeeRepository.findAll();
        
        return allEmployees.stream()
            .filter(skillFilter)
            .filter(salaryFilter)
            .map(employeeMapper)
            .collect(Collectors.toList());
    }

    // 3. Advanced Employee Grouping and Aggregation
    public Map<String, List<Employee>> groupEmployeesByComplexCriteria(
            Function<Employee, String> groupingFunction,
            Predicate<Employee> filterCondition) {
        
        return employeeRepository.findAll().stream()
            .filter(filterCondition)
            .collect(Collectors.groupingBy(
                groupingFunction, 
                Collectors.toList()
            ));
    }

    // 4. Functional Salary Adjustment Strategy
    public List<Employee> applyConditionalSalaryAdjustment(
            Predicate<Employee> eligibilityCriteria,
            Function<Employee, Double> salaryAdjustmentStrategy) {
        
        return employeeRepository.findAll().stream()
            .map(employee -> {
                if (eligibilityCriteria.test(employee)) {
                    double adjustedSalary = salaryAdjustmentStrategy.apply(employee);
                    employee.setSalary(adjustedSalary);
                }
                return employee;
            })
            .collect(Collectors.toList());
    }

    // 5. Advanced Functional Composition for Employee Evaluation
    public List<String> evaluateEmployeesWithComplexRules(
            Predicate<Employee> seniorityFilter,
            Predicate<Employee> performanceFilter,
            Function<Employee, String> evaluationMapper) {
        
        return employeeRepository.findAll().stream()
            .filter(seniorityFilter)
            .filter(performanceFilter)
            .map(evaluationMapper)
            .collect(Collectors.toList());
    }

    // Demonstration Method
    public void demonstrateAdvancedFunctionalProgramming() {
        // Example 1: Filter high-performing, senior employees with high salaries
        List<String> seniorHighPerformers = processEmployeesWithAdvancedFiltering(
            emp -> emp.getPosition().contains("Senior"),
            emp -> emp.getSalary() > 100000,
            Employee::getFullName
        );

        // Example 2: Group employees by complex criteria
        Map<String, List<Employee>> employeesByDepartmentAndSeniority = groupEmployeesByComplexCriteria(
            emp -> emp.getPosition() + "_" + (emp.getSalary() > 80000 ? "Senior" : "Junior"),
            emp -> emp.getFirstName().length() > 3
        );

        // Example 3: Apply conditional salary adjustment
        List<Employee> adjustedEmployees = applyConditionalSalaryAdjustment(
            emp -> emp.getPosition().contains("Junior") && emp.getSalary() < 70000,
            emp -> emp.getSalary() * 1.15  // 15% raise for junior employees
        );

        // Example 4: Complex employee evaluation
        List<String> evaluatedEmployees = evaluateEmployeesWithComplexRules(
            emp -> emp.getPosition().contains("Senior"),
            emp -> new BigDecimal(emp.getSalary()).compareTo(BigDecimal.valueOf(90000)) > 0,
            emp -> String.format("Employee: %s, Salary: %.2f, Position: %s", 
                emp.getFullName(), emp.getSalary(), emp.getPosition())
        );
    }
}