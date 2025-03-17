package com.example.cruddemo.service.impl;

import com.example.cruddemo.exception.ResourceNotFoundException;
import com.example.cruddemo.model.Manager;
import com.example.cruddemo.repository.ManagerRepository;
import com.example.cruddemo.service.ManagerService;
import com.example.cruddemo.util.AppLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional
    public Manager createManager(Manager manager) {
        AppLogger.log1Info("Creating new manager: " + manager.getFirstName() + " " + manager.getLastName());
        return managerRepository.save(manager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manager> getAllManagers() {
        AppLogger.log1Info("Fetching all managers");
        return managerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Manager> getManagerById(Long id) {
        AppLogger.log1Info("Fetching manager with ID: " + id);
        return managerRepository.findById(id);
    }

    @Override
    @Transactional
    public Manager updateManager(Long id, Manager managerDetails) {
        Manager existingManager = managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + id));

        // Update manager details
        existingManager.setFirstName(managerDetails.getFirstName());
        existingManager.setLastName(managerDetails.getLastName());
        existingManager.setEmail(managerDetails.getEmail());
        existingManager.setPhoneNumber(managerDetails.getPhoneNumber());
        existingManager.setSalary(managerDetails.getSalary());

        AppLogger.log1Info("Updating manager with ID: " + id);
        return managerRepository.save(existingManager);
    }

    @Override
    @Transactional
    public void deleteManager(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + id));

        AppLogger.log1Info("Deleting manager with ID: " + id);
        managerRepository.delete(manager);
    }
}
