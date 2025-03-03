package com.example.cruddemo.service;

import com.example.cruddemo.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    
    List<Employee> getAllEmployees();
    
    Optional<Employee> getEmployeeById(Long id);
    
    Employee createEmployee(Employee employee);
    
    Employee updateEmployee(Long id, Employee employeeDetails);
    
    void deleteEmployee(Long id);
    
    List<Employee> findByLastName(String lastName);
    
    List<Employee> findByPosition(String position);
    
    Employee findByEmail(String email);
}
