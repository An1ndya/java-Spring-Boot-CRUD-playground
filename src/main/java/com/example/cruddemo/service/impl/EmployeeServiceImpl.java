package com.example.cruddemo.service.impl;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Employee;
import com.example.cruddemo.repository.EmployeeRepository;
import com.example.cruddemo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeService interface.
 * Provides business logic for employee-related operations.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    // Repository dependency
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor-based dependency injection
     * @param employeeRepository The repository for employee data access
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        logger.info("EmployeeServiceImpl initialized with repository");
    }

    /**
     * Retrieves all employees from the database
     * @return List of all employees
     */
    @Override
    public List<Employee> getAllEmployees() {
        logger.debug("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.debug("Found {} employees", employees.size());
        return employees;
    }

    /**
     * Retrieves an employee by their ID
     * @param id The ID of the employee to retrieve
     * @return Optional containing the employee if found
     */
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        logger.debug("Fetching employee with id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            logger.debug("Found employee: {}", employee.get().getEmail());
        } else {
            logger.debug("No employee found with id: {}", id);
        }
        return employee;
    }

    /**
     * Creates a new employee in the database
     * @param employee The employee entity to create
     * @return The created employee with generated ID
     */
    @Override
    public Employee createEmployee(Employee employee) {
        logger.debug("Creating new employee with email: {}", employee.getEmail());
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee created successfully with id: {}", savedEmployee.getId());
        return savedEmployee;
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
        logger.debug("Updating employee with id: {}", id);
        
        // Find the employee by ID or throw exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Failed to update - Employee not found with id: {}", id);
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
        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Employee updated successfully: {}", updatedEmployee.getId());
        return updatedEmployee;
    }

    /**
     * Deletes an employee from the database
     * @param id The ID of the employee to delete
     * @throws ResourceNotFoundException if employee not found
     */
    @Override
    public void deleteEmployee(Long id) {
        logger.debug("Deleting employee with id: {}", id);
        
        // Find the employee by ID or throw exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Failed to delete - Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        
        // Delete the employee
        employeeRepository.delete(employee);
        logger.info("Employee deleted successfully with id: {}", id);
    }

    /**
     * Finds employees by their last name
     * @param lastName The last name to search for
     * @return List of employees with matching last name
     */
    @Override
    public List<Employee> findByLastName(String lastName) {
        logger.debug("Finding employees with last name: {}", lastName);
        List<Employee> employees = employeeRepository.findByLastName(lastName);
        logger.debug("Found {} employees with last name: {}", employees.size(), lastName);
        return employees;
    }

    /**
     * Finds employees by their position
     * @param position The position to search for
     * @return List of employees with matching position
     */
    @Override
    public List<Employee> findByPosition(String position) {
        logger.debug("Finding employees with position: {}", position);
        List<Employee> employees = employeeRepository.findByPosition(position);
        logger.debug("Found {} employees with position: {}", employees.size(), position);
        return employees;
    }

    /**
     * Finds an employee by their email address
     * @param email The email to search for
     * @return The employee with matching email or null if not found
     */
    @Override
    public Employee findByEmail(String email) {
        logger.debug("Finding employee with email: {}", email);
        Employee employee = employeeRepository.findByEmail(email);
        if (employee != null) {
            logger.debug("Found employee with id: {}", employee.getId());
        } else {
            logger.debug("No employee found with email: {}", email);
        }
        return employee;
    }
}
