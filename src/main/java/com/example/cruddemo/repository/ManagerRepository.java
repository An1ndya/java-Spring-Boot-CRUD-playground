package com.example.cruddemo.repository;

import com.example.cruddemo.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // No additional methods needed, using standard JpaRepository methods
}
