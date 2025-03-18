package com.example.cruddemo.service.impl;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Employee;
import com.example.cruddemo.model.Manager;
import com.example.cruddemo.repository.EmployeeRepository;
import com.example.cruddemo.repository.ManagerRepository;
import com.example.cruddemo.service.EmployeeService;
import com.example.cruddemo.util.AppLogger;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.validation.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeService interface.
 * Provides business logic for employee-related operations.
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    // Repository dependency
    private final EmployeeRepository employeeRepository;
    
    // Entity manager for stored procedure calls
    private final EntityManager entityManager;
    
    // Manager repository for manager updates
    private final ManagerRepository managerRepository;
    
    // Hibernate session for stored procedure calls
    @Autowired
    private Session session;

    /**
     * Constructor-based dependency injection
     * @param employeeRepository The repository for employee data access
     * @param entityManager The entity manager for stored procedure execution
     * @param managerRepository The repository for manager data access
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager, ManagerRepository managerRepository) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
        this.managerRepository = managerRepository;
                
        AppLogger.log1Info("EmployeeServiceImpl initialized with repository, entity manager, and manager repository");
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
            //session.beginTransaction(); // This create error as shared entity manager
            
            StoredProcedureQuery query = session.createStoredProcedureQuery("sp_insert_employee", Employee.class)
                    .registerStoredProcedureParameter("first_name_param", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("last_name_param", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("email_param", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("phone_number_param", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("position_param", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("salary_param", BigDecimal.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("manager_id_param", Long.class, ParameterMode.IN)  // New parameter for manager ID
                    .setParameter("first_name_param", employee.getFirstName())
                    .setParameter("last_name_param", employee.getLastName())
                    .setParameter("email_param", employee.getEmail())
                    .setParameter("phone_number_param", employee.getPhoneNumber())
                    .setParameter("position_param", employee.getPosition())
                    .setParameter("salary_param", BigDecimal.valueOf(employee.getSalary()))
                    .setParameter("manager_id_param", employee.getManagerId());  
            
            // Execute the stored procedure
            query.execute();
            //session.getTransaction().commit();
            
            // Retrieve the inserted employee
            Object queryObject = query.getSingleResult();
            Employee insertedEmployee = mapQueryResultToEntity(queryObject, Employee.class);
            //Employee insertedEmployee = (Employee) query.getSingleResult();
            
            return insertedEmployee;
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
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
    @Transactional(rollbackFor = {Exception.class})
    @Lock(LockModeType.PESSIMISTIC_READ) 
    @Query("SELECT * FROM employees WHERE id = :id")
    public Employee updateEmployee(@Param("id") Long id, Employee employeeDetails) {
        AppLogger.log1Info("Service: Attempting to update employee with ID: {}", id);
        
        try {
            // Validate input
            validateEmployeeUpdateInput(employeeDetails);
            
            // Find the employee by ID or throw exception if not found
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> {
                        AppLogger.log1Error("Employee not found with id: {}", id);
                        return new ResourceNotFoundException("Employee not found with id: " + id);
                    });
            
            // Perform update with null-safe checks
            updateEmployeeFields(employee, employeeDetails);
            
            // Save the updated employee
            Employee updatedEmployee = employeeRepository.save(employee);
            
            AppLogger.log2Info("Successfully updated employee with ID: {}", id);
            return updatedEmployee;
        
        } catch (ResourceNotFoundException e) {
            // Specific handling for employee not found
            AppLogger.log1Error("Update failed - Employee not found: {}", e.getMessage());
            throw e;
        
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            // Handle concurrent modification attempts
            AppLogger.log1Error("Concurrent modification detected for employee ID: {}", id);
            throw new ConcurrentModificationException("Another user is updating this employee. Please try again.", e);
        
        } catch (org.springframework.transaction.TransactionSystemException e) {
            // Handle transaction-related exceptions
            AppLogger.log1Error("Transaction failed during employee update: {}", e.getMessage());
            throw new TransactionException("Transaction failed. Changes were not saved.", e);
        
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle database constraint violations
            AppLogger.log1Error("Data integrity violation during update: {}", e.getMessage());
            throw new ValidationException("Update violates data integrity constraints.", e);
        
        } catch (Exception e) {
            // Catch-all for unexpected exceptions
            AppLogger.log1Error("Unexpected error during employee update: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred during update.", e);
        }
    }
    
    /**
     * Validates input for employee update
     * @param employeeDetails Employee details to validate
     * @throws ValidationException if validation fails
     */
    private void validateEmployeeUpdateInput(Employee employeeDetails) {
        if (employeeDetails == null) {
            throw new IllegalArgumentException("Employee update details cannot be null");
        }
        
        // Validate specific fields
        if (employeeDetails.getEmail() != null && !isValidEmail(employeeDetails.getEmail())) {
            throw new ValidationException("Invalid email format");
        }
        
        if (employeeDetails.getSalary() != null && employeeDetails.getSalary() < 0) {
            throw new ValidationException("Salary cannot be negative");
        }
        
        // Validate manager if provided
        if (employeeDetails.getManager() != null) {
            // Verify manager exists in the database
            managerRepository.findById(employeeDetails.getManager().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Manager not found with id: " + employeeDetails.getManager().getId()));
            
            AppLogger.log2Info("Updating employee with manager ID: {}", 
                employeeDetails.getManager().getId());
        }
    }
    
    /**
     * Performs null-safe update of employee fields
     * @param existingEmployee Current employee entity
     * @param updateDetails Employee details to update
     */
    private void updateEmployeeFields(Employee existingEmployee, Employee updateDetails) {
        if (updateDetails.getFirstName() != null) {
            existingEmployee.setFirstName(updateDetails.getFirstName());
        }
        if (updateDetails.getLastName() != null) {
            existingEmployee.setLastName(updateDetails.getLastName());
        }
        if (updateDetails.getEmail() != null) {
            existingEmployee.setEmail(updateDetails.getEmail());
        }
        if (updateDetails.getPhoneNumber() != null) {
            existingEmployee.setPhoneNumber(updateDetails.getPhoneNumber());
        }
        if (updateDetails.getPosition() != null) {
            existingEmployee.setPosition(updateDetails.getPosition());
        }
        if (updateDetails.getSalary() != null) {
            existingEmployee.setSalary(updateDetails.getSalary());
        }
        // Update manager if provided
        if (updateDetails.getManager() != null) {
            // Validate manager existence in the database
            Manager existingManager = managerRepository.findById(updateDetails.getManager().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Manager not found with id: " + updateDetails.getManager().getId()));
            
            existingEmployee.setManager(existingManager);
        }
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

    /**
     * Maps a database query result to an entity model object
     * 
     * @param queryResult The object returned from a database query
     * @param entityClass The class of the model entity to map to
     * @param <T> The type of the entity model
     * @return The mapped entity model object
     * @throws IllegalArgumentException if mapping fails
     */
    public <T> T mapQueryResultToEntity(Object queryResult, Class<T> entityClass) {
        AppLogger.log1Info("Service: Mapping query result to entity of type: {}", entityClass.getSimpleName());
        
        try {
            // Create a new instance of the target entity class
            T entityInstance = entityClass.getDeclaredConstructor().newInstance();
            
            // Use reflection to copy properties from queryResult to entityInstance
            for (java.lang.reflect.Method method : entityClass.getDeclaredMethods()) {
                if (method.getName().startsWith("set")) {
                    String propertyName = method.getName().substring(3);
                    propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
                    
                    try {
                        java.lang.reflect.Method getterMethod = queryResult.getClass().getMethod("get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1));
                        Object value = getterMethod.invoke(queryResult);
                        
                        method.invoke(entityInstance, value); 
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        // Log warning if property cannot be mapped, but continue processing
                        AppLogger.log2Warn("Could not map property {} for {}", propertyName, entityClass.getSimpleName());
                    }
                }
            }
            
            AppLogger.log2Info("Successfully mapped query result to {}", entityClass.getSimpleName());
            return entityInstance;
        } catch (Exception e) {
            AppLogger.log1Error("Error mapping query result to entity: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to map query result to entity: " + e.getMessage(), e);
        }
    }
}
