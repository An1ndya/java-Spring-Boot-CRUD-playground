package com.example.cruddemo.controller;

import com.example.cruddemo.model.Manager;
import com.example.cruddemo.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/managers")
@Slf4j
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
        log.info("ManagerController initialized with service");
    }

    @PostMapping
    public ResponseEntity<Manager> createManager(@Valid @RequestBody Manager manager) {
        log.info("Creating new manager: {} {} with salary: {}", 
                manager.getFirstName(), 
                manager.getLastName(), 
                manager.getSalary());
        return new ResponseEntity<>(managerService.createManager(manager), HttpStatus.CREATED);
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long managerId) {
        log.info("Fetching manager with ID: {}", managerId);
        return managerService.getManagerById(managerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<Manager> updateManager(
            @PathVariable Long managerId, 
            @Valid @RequestBody Manager managerDetails) {
        log.info("Updating manager with ID: {} with new salary: {}", managerId, managerDetails.getSalary());
        return ResponseEntity.ok(managerService.updateManager(managerId, managerDetails));
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long managerId) {
        log.info("Deleting manager with ID: {}", managerId);
        managerService.deleteManager(managerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        log.info("Fetching all managers");
        return ResponseEntity.ok(managerService.getAllManagers());
    }
}
