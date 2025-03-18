-- Stored Procedures for Employee CRUD Operations

-- Procedure to insert a new employee
DELIMITER //
DROP PROCEDURE IF EXISTS sp_insert_employee;
Create PROCEDURE sp_insert_employee(
    IN first_name_param VARCHAR(255),
    IN last_name_param VARCHAR(255),
    IN email_param VARCHAR(255),
    IN phone_number_param VARCHAR(20),
    IN position_param VARCHAR(255),
    IN salary_param DECIMAL(10,2),
    IN manager_id_param int 
)
BEGIN
    INSERT INTO employees (first_name, last_name, email, phone_number, position, salary, manager_id)
    VALUES (first_name_param, last_name_param, email_param, phone_number_param, position_param, salary_param, manager_id_param);
    
    SELECT * FROM employees WHERE id = LAST_INSERT_ID();
END //
DELIMITER ;


