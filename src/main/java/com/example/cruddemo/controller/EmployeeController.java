package com.example.cruddemo.controller;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Employee;
import com.example.cruddemo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Employee resources.
 * Provides endpoints for CRUD operations on employees.
 */
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    // Service dependency
    private final EmployeeService employeeService;

    /**
     * Constructor-based dependency injection
     * @param employeeService The service for employee business logic
     */
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        logger.info("EmployeeController initialized with service");
    }

    /**
     * Get all employees endpoint
     * @return List of all employees with HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("REST request to get all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.debug("Retrieved {} employees", employees.size());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Get employee by ID endpoint
     * @param employeeId The ID of the employee to retrieve
     * @return The employee with HTTP status
     * @throws ResourceNotFoundException if employee not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) {
        logger.info("REST request to get Employee with id: {}", employeeId);
        
        Employee employee = employeeService.getEmployeeById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", employeeId);
                    return new ResourceNotFoundException("Employee not found with id: " + employeeId);
                });
                
        return ResponseEntity.ok().body(employee);
    }

    /**
     * Create new employee endpoint
     * @param employee The employee entity to create
     * @return The created employee with HTTP status
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        logger.info("REST request to create Employee: {}", employee.getEmail());
        Employee savedEmployee = employeeService.createEmployee(employee);
        logger.debug("Created Employee with id: {}", savedEmployee.getId());
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    /**
     * Update employee endpoint
     * @param employeeId The ID of the employee to update
     * @param employeeDetails The updated employee details
     * @return The updated employee with HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable(value = "id") Long employeeId,
            @Valid @RequestBody Employee employeeDetails) {
        
        logger.info("REST request to update Employee with id: {}", employeeId);
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDetails);
        logger.debug("Updated Employee: {}", updatedEmployee.getId());
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Delete employee endpoint
     * @param employeeId The ID of the employee to delete
     * @return Confirmation message with HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        logger.info("REST request to delete Employee with id: {}", employeeId);
        
        employeeService.deleteEmployee(employeeId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        logger.debug("Deleted Employee with id: {}", employeeId);
        return ResponseEntity.ok(response);
    }

    /**
     * Find employees by last name endpoint
     * @param lastName The last name to search for
     * @return List of employees with matching last name
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Employee>> getEmployeesByLastName(@PathVariable String lastName) {
        logger.info("REST request to get Employees by last name: {}", lastName);
        
        List<Employee> employees = employeeService.findByLastName(lastName);
        logger.debug("Found {} employees with last name: {}", employees.size(), lastName);
        return ResponseEntity.ok(employees);
    }

    /**
     * Find employees by position endpoint
     * @param position The position to search for
     * @return List of employees with matching position
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        logger.info("REST request to get Employees by position: {}", position);
        
        List<Employee> employees = employeeService.findByPosition(position);
        logger.debug("Found {} employees with position: {}", employees.size(), position);
        return ResponseEntity.ok(employees);
    }

    /**
     * Find employee by email endpoint
     * @param email The email to search for
     * @return The employee with matching email
     * @throws ResourceNotFoundException if employee not found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        logger.info("REST request to get Employee by email: {}", email);
        
        Employee employee = employeeService.findByEmail(email);
        if (employee == null) {
            logger.error("Employee not found with email: {}", email);
            throw new ResourceNotFoundException("Employee not found with email: " + email);
        }
        
        logger.debug("Found Employee with id: {}", employee.getId());
        return ResponseEntity.ok(employee);
    }
}
