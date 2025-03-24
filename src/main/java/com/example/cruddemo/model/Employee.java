package com.example.cruddemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

@Entity
@Table(name = "employees")
@NamedQueries({
    @NamedQuery(name = "Employee.findByPosition", 
                query = "FROM Employee e WHERE e.position = :position"),
    @NamedQuery(name = "Employee.findHighPaidEmployees", 
                query = "FROM Employee e WHERE e.salary > :salaryThreshold")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String position;

    @Column
    private Double salary;

    @Column(name = "manager_id", insertable = false, updatable = false)
    private Long managerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
    private Manager manager;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary != null ? salary.doubleValue() : null;
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Long getManagerId() {
        return this.manager != null ? this.manager.getId() : null;
    }

    public void setManagerId(Long managerId) {
        if (this.manager == null) {
            this.manager = new Manager();
        }
        this.manager.setId(managerId);
    }

    // Method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Java 8 Functional Interface Methods
    public boolean isHighEarner(BigDecimal threshold) {
        return Optional.ofNullable(this.salary)
            .map(sal -> sal.compareTo(threshold.doubleValue()) > 0)
            .orElse(false);
    }

    // Lambda-based filter predicate
    public static Predicate<Employee> salaryFilter(BigDecimal threshold) {
        return employee -> employee.isHighEarner(threshold);
    }

    public Employee(Object object, String string, String string2, String string3, String string4, double d,
            String string5) {
        this.firstName = string;
        this.lastName = string2;
        this.email = string3;
        this.position = string4;
        this.phoneNumber = string5;
        this.salary = d;
        //this.managerId = Long.valueOf(string6);
    }


}
