-- Create employee table
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    position VARCHAR(255),
    salary DECIMAL(10,2)
);

DELIMITER //
CREATE PROCEDURE CreateEmployee(
    IN firstName VARCHAR(255), 
    IN lastName VARCHAR(255), 
    IN email VARCHAR(255), 
    IN phoneNumber VARCHAR(20), 
    IN position VARCHAR(255), 
    IN salary DECIMAL(10,2),
    OUT newId INT  -- Added OUT parameter
)
BEGIN
    INSERT INTO employees (first_name, last_name, email, phone_number, position, salary) 
    VALUES (firstName, lastName, email, phoneNumber, position, salary);

    SET newId = LAST_INSERT_ID();  -- Assign last inserted ID to OUT parameter
END //
DELIMITER ;
