package com.example.cruddemo.service.impl;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Manager;
import com.example.cruddemo.repository.ManagerRepository;
import com.example.cruddemo.service.ManagerService;
import com.example.cruddemo.util.AppLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
        AppLogger.log1Info("ManagerServiceImpl initialized with repository");
    }

    @Override
    @Transactional
    public Manager createManager(Manager manager) {
        AppLogger.log1Info("Creating new manager: " + manager.getFirstName() + " " + manager.getLastName());
        return managerRepository.save(manager);
    }

    @Override
    public Optional<Manager> getManagerById(Long managerId) {
        AppLogger.log1Info("Fetching manager with ID: " + managerId);
        return managerRepository.findById(managerId);
    }

    @Override
    @Transactional
    public Manager updateManager(Long managerId, Manager managerDetails) {
        Manager existingManager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + managerId));

        // Update manager details
        existingManager.setFirstName(managerDetails.getFirstName());
        existingManager.setLastName(managerDetails.getLastName());
        existingManager.setEmail(managerDetails.getEmail());
        existingManager.setPhoneNumber(managerDetails.getPhoneNumber());

        AppLogger.log1Info("Updating manager with ID: " + managerId);
        return managerRepository.save(existingManager);
    }

    @Override
    @Transactional
    public void deleteManager(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + managerId));

        AppLogger.log1Info("Deleting manager with ID: " + managerId);
        managerRepository.delete(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        AppLogger.log1Info("Fetching all managers");
        return managerRepository.findAll();
    }
}
