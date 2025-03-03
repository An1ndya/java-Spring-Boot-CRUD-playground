package com.example.cruddemo.repository;

import com.example.cruddemo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Custom query methods
    List<Employee> findByLastName(String lastName);
    
    List<Employee> findByPosition(String position);
    
    Employee findByEmail(String email);
}
