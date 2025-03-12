package com.example.cruddemo.service.impl;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import com.example.cruddemo.service.EmployeeService;
import com.example.cruddemo.util.AppLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeService interface.
 * Provides business logic for employee-related operations.
 */
@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    // Repository dependency
    private final EmployeeRepository employeeRepository;
    
    // Entity manager for stored procedure calls
    private final EntityManager entityManager;

    /**
     * Constructor-based dependency injection
     * @param employeeRepository The repository for employee data access
     * @param entityManager The entity manager for stored procedure execution
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
                
        AppLogger.log1Info("EmployeeServiceImpl initialized with repository and entity manager");
    }

    /**
     * Retrieves all employees from the database
     * @return List of all employees
     */
    @Override
    public List<Employee> getAllEmployees() {
        AppLogger.log1Info("Service: Getting all employees");
        List<Employee> employees = employeeRepository.findAll();
        AppLogger.log2Info("Service: Found {} employees", employees.size());
        return employees;
    }

    /**
     * Retrieves an employee by their ID
     * @param id The ID of the employee to retrieve
     * @return Optional containing the employee if found
     */
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        AppLogger.log1Info("Service: Getting employee with ID: " + id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            AppLogger.log2Info("Service: Found employee: {}", employee.get().getEmail());
        } else {
            AppLogger.log2Info("Service: No employee found with id: {}", id);
        }
        return employee;
    }

    /**
     * Creates a new employee in the database using stored procedure
     * @param employee The employee entity to create
     * @return The created employee with generated ID
     */
    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        AppLogger.log1Info("Service: Attempting to create new employee: {} {}", employee.getFirstName(), employee.getLastName());
        AppLogger.log2Info("Service: Full employee details: {}", employee);
        
        try {
            // Validate input
            validateEmployeeInput(employee);
            
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CreateEmployee")
                    .registerStoredProcedureParameter("firstName", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("lastName", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("email", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("phoneNumber", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("position", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("salary", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("newId", Integer.class, ParameterMode.OUT)
                    .setParameter("firstName", employee.getFirstName())
                    .setParameter("lastName", employee.getLastName())
                    .setParameter("email", employee.getEmail())
                    .setParameter("phoneNumber", employee.getPhoneNumber())
                    .setParameter("position", employee.getPosition())
                    .setParameter("salary", BigDecimal.valueOf(employee.getSalary()));
            
            // Execute the stored procedure
            query.execute();
            
            // Retrieve the generated ID
            Object newIdObj = query.getOutputParameterValue("newId");
            
            if (newIdObj == null) {
                throw new RuntimeException("Failed to generate employee ID");
            }
            
            if (!(newIdObj instanceof Integer)) {
                throw new Exception("Generated ID is not of type Integer. Actual type: " + 
                    (newIdObj != null ? newIdObj.getClass().getName() : "null"));
            }
            
            Integer id = (Integer) newIdObj;
            employee.setId(id.longValue());
            
            AppLogger.log1Info("Service: Employee created successfully with ID: {}", id);
            return employee;
        } catch (RuntimeException e) {
            AppLogger.log1Error("Error creating employee: {}", e.getMessage());
            
            // Detailed error logging
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            AppLogger.log2Error("Detailed error trace: {}", sw.toString());
            
            throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
        }
        catch (Exception e) {
            AppLogger.log1Error("Error creating employee: {}", e.getMessage());
            
            // Detailed error logging
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            AppLogger.log2Error("Detailed error trace: {}", sw.toString());
            
            throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates employee input before creating
     * @param employee The employee to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateEmployeeInput(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        
        // First Name validation
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        // Last Name validation
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        // Email validation
        if (employee.getEmail() == null || !isValidEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }
        
        // Salary validation
        if (employee.getSalary() != null && employee.getSalary() < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }
    
    /**
     * Basic email validation
     * @param email Email to validate
     * @return true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Updates an existing employee's information
     * @param id The ID of the employee to update
     * @param employeeDetails The updated employee details
     * @return The updated employee entity
     * @throws ResourceNotFoundException if employee not found
     */
    @Override
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        AppLogger.log1Info("Service: Updating employee with ID: " + id);
        
        // Find the employee by ID or throw exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    AppLogger.log1Error("Employee not found with id: " + id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        
        // Update employee fields
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());
        
        // Save and return the updated employee
        AppLogger.log2Info("Service: Updated employee details: " + employee); // More detailed log in secondary log
        Employee updatedEmployee = employeeRepository.save(employee);
        AppLogger.log1Info("Service: Employee updated successfully: {}", updatedEmployee.getId());
        return updatedEmployee;
    }

    /**
     * Deletes an employee from the database
     * @param id The ID of the employee to delete
     * @throws ResourceNotFoundException if employee not found
     */
    @Override
    public void deleteEmployee(Long id) {
        AppLogger.log1Info("Service: Deleting employee with ID: " + id);
        
        // Find the employee by ID or throw exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    AppLogger.log1Error("Employee not found with id: " + id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        
        // Delete the employee
        employeeRepository.delete(employee);
        AppLogger.log2Info("Service: Employee deleted: " + employee.getId());
    }

    /**
     * Finds employees by their last name
     * @param lastName The last name to search for
     * @return List of employees with matching last name
     */
    @Override
    public List<Employee> findByLastName(String lastName) {
        AppLogger.log1Info("Service: Finding employees with last name: {}", lastName);
        List<Employee> employees = employeeRepository.findByLastName(lastName);
        AppLogger.log2Info("Service: Found {} employees with last name: {}", employees.size(), lastName);
        return employees;
    }

    /**
     * Finds employees by their position
     * @param position The position to search for
     * @return List of employees with matching position
     */
    @Override
    public List<Employee> findByPosition(String position) {
        AppLogger.log1Info("Service: Finding employees with position: {}", position);
        List<Employee> employees = employeeRepository.findByPosition(position);
        AppLogger.log2Info("Service: Found {} employees with position: {}", employees.size(), position);
        return employees;
    }

    /**
     * Finds an employee by their email address
     * @param email The email to search for
     * @return The employee with matching email or null if not found
     */
    @Override
    public Employee findByEmail(String email) {
        AppLogger.log1Info("Service: Finding employee with email: {}", email);
        Employee employee = employeeRepository.findByEmail(email);
        if (employee != null) {
            AppLogger.log2Info("Service: Found employee with id: {}", employee.getId());
        } else {
            AppLogger.log2Info("Service: No employee found with email: {}", email);
        }
        return employee;
    }
}
