package com.example.cruddemo.controller;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Employee;
import com.example.cruddemo.service.EmployeeService;
import com.example.cruddemo.util.AppLogger;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EmployeeController {

    // Service dependency
    private final EmployeeService employeeService;

    /**
     * Constructor-based dependency injection
     * @param employeeService The employee service to be used
     */
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        log.info("EmployeeController initialized with service");
    }

    /**
     * Get all employees endpoint
     * @return List of all employees with HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        AppLogger.log1Info("Fetching all employees");
        List<Employee> employees = employeeService.getAllEmployees();
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
        AppLogger.log1Info("Fetching employee with ID: " + employeeId);
        Employee employee = employeeService.getEmployeeById(employeeId)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", employeeId);
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
        AppLogger.log1Info("Creating new employee: " + employee.getFirstName() + " " + employee.getLastName());
        AppLogger.log2Info("Employee creation details: " + employee); // Log details to secondary log
        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    /**
     * Update employee endpoint
     * @param id The ID of the employee to update
     * @param employeeDetails The updated employee details
     * @return The updated employee with HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable(value = "id") Long employeeId, 
            @Valid @RequestBody Employee employeeDetails) {
        
        AppLogger.log1Info("Updating employee with ID: " + employeeId);
        AppLogger.log2Info("Employee update details: " + employeeDetails); // Log details to secondary log
        
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Delete employee endpoint
     * @param employeeId The ID of the employee to delete
     * @return Confirmation message with HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        AppLogger.log1Info("Deleting employee with ID: " + employeeId);
        employeeService.deleteEmployee(employeeId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    /**
     * Find employees by last name endpoint
     * @param lastName The last name to search for
     * @return List of employees with matching last name
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Employee>> getEmployeesByLastName(@PathVariable String lastName) {
        log.info("REST request to get Employees by last name: {}", lastName);
        
        List<Employee> employees = employeeService.findByLastName(lastName);
        log.debug("Found {} employees with last name: {}", employees.size(), lastName);
        return ResponseEntity.ok(employees);
    }

    /**
     * Find employees by position endpoint
     * @param position The position to search for
     * @return List of employees with matching position
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        log.info("REST request to get Employees by position: {}", position);
        
        List<Employee> employees = employeeService.findByPosition(position);
        log.debug("Found {} employees with position: {}", employees.size(), position);
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
        log.info("REST request to get Employee by email: {}", email);
        
        Employee employee = employeeService.findByEmail(email);
        if (employee == null) {
            log.error("Employee not found with email: {}", email);
            throw new ResourceNotFoundException("Employee not found with email: " + email);
        }
        
        log.debug("Found Employee with id: {}", employee.getId());
        return ResponseEntity.ok(employee);
    }

    /**
     * Find high-paid employees endpoint
     * @param salaryThreshold The salary threshold to filter employees
     * @return List of names of high-paid employees
     */
    @GetMapping("/high-paid")
    public ResponseEntity<List<String>> getHighPaidEmployeeNames(
            @RequestParam(value = "threshold", defaultValue = "50000.0") Double salaryThreshold) {
        log.info("REST request to get high-paid employee names with threshold: {}", salaryThreshold);
        
        List<String> highPaidEmployeeNames = employeeService.findHighPaidEmployeeNames(salaryThreshold);
        log.debug("Found {} high-paid employees", highPaidEmployeeNames.size());
        
        return ResponseEntity.ok(highPaidEmployeeNames);
    }

    /**
     * Find the highest paid employee endpoint
     * @return The employee with the highest salary
     */
    @GetMapping("/highest-paid")
    public ResponseEntity<Employee> getHighestPaidEmployee() {
        log.info("REST request to get highest paid employee");
        
        Employee highestPaidEmployee = employeeService.findHighestPaidEmployee()
            .orElseThrow(() -> new ResourceNotFoundException("No employees found"));
        
        log.debug("Highest paid employee found: {}", highestPaidEmployee.getFullName());
        return ResponseEntity.ok(highestPaidEmployee);
    }

    /**
     * Find employees under a specific manager endpoint
     * @param managerId The ID of the manager
     * @return List of full names of employees under the manager
     */
    @GetMapping("/under-manager/{managerId}")
    public ResponseEntity<List<String>> getEmployeesUnderManager(@PathVariable Long managerId) {
        log.info("REST request to get employees under manager with ID: {}", managerId);
        
        List<String> employeesUnderManager = employeeService.findEmployeesUnderManager(managerId);
        log.debug("Found {} employees under manager", employeesUnderManager.size());
        
        return ResponseEntity.ok(employeesUnderManager);
    }
}
