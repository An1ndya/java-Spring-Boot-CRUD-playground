package com.example.cruddemo.repository;

import com.example.cruddemo.model.Employee;
import com.example.cruddemo.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Existing methods
    List<Employee> findByPosition(String position);
    
    Employee findByEmail(String email);

    // Native SQL Query for high-paid employees using Double
    @Query(value = "SELECT * FROM employees e WHERE e.salary > :salaryThreshold", nativeQuery = true)
    List<Employee> findHighPaidEmployees(@Param("salaryThreshold") Double salaryThreshold);

    // Find employees by manager
    @Query("FROM Employee e WHERE e.manager.id = :managerId")
    List<Employee> findByManagerId(@Param("managerId") Long managerId);

    // Find employees by manager entity
    List<Employee> findByManager(Manager manager);

    List<Employee> findByLastName(String lastName);
}
