package com.example.cruddemo.service;

import com.example.cruddemo.model.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    // Create a new manager
    Manager createManager(Manager manager);

    // Get manager by ID
    Optional<Manager> getManagerById(Long managerId);

    // Update manager
    Manager updateManager(Long managerId, Manager managerDetails);

    // Delete manager
    void deleteManager(Long managerId);

    // Get all managers
    List<Manager> getAllManagers();
}
