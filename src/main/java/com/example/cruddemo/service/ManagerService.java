package com.example.cruddemo.service;

import com.example.cruddemo.model.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    // Create a new manager
    Manager createManager(Manager manager);

    // Get manager by ID
    Optional<Manager> getManagerById(Long id);

    // Update manager
    Manager updateManager(Long id, Manager managerDetails);

    // Delete manager
    void deleteManager(Long id);

    // Get all managers
    List<Manager> getAllManagers();
}
