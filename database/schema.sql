-- ========================================================
-- Employee Payroll Management System - Database Schema & Demo Data
-- Group 10
-- ========================================================

CREATE DATABASE IF NOT EXISTS payroll_db;
USE payroll_db;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL UNIQUE
);

-- 2. Employees Table
CREATE TABLE IF NOT EXISTS employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('Manager', 'Clerk', 'Teacher')),
    dept_id INT,
    basic_salary DOUBLE NOT NULL,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- 3. Salary Table
CREATE TABLE IF NOT EXISTS salary (
    salary_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT NOT NULL,
    month VARCHAR(20) NOT NULL,
    year INT NOT NULL,
    basic DOUBLE NOT NULL,
    hra DOUBLE NOT NULL,
    da DOUBLE NOT NULL,
    gross DOUBLE NOT NULL,
    pf DOUBLE NOT NULL,
    tax DOUBLE NOT NULL,
    net_salary DOUBLE NOT NULL,
    FOREIGN KEY (emp_id) REFERENCES employees(emp_id) ON DELETE CASCADE
);

-- ========================================================
-- DEMO DATA SEEDING
-- ========================================================

-- Insert Departments
INSERT INTO departments (dept_id, dept_name) VALUES
    (1, 'Information Technology'),
    (2, 'Finance & Accounts'),
    (3, 'Human Resources'),
    (4, 'Administration'),
    (5, 'Academics')
ON DUPLICATE KEY UPDATE dept_name = VALUES(dept_name);

-- Insert Demo Employees (Including Team Members with realistic designations & departments)
INSERT INTO employees (emp_id, name, type, dept_id, basic_salary) VALUES
    (101, 'Rohit Kharnotia (Leader)', 'Manager', 1, 75000.00),
    (102, 'Sankeit Sharma',            'Clerk',   2, 35000.00),
    (103, 'Aryan',                     'Teacher', 5, 50000.00),
    (104, 'Gaganpreet Singh',          'Manager', 4, 70000.00),
    (105, 'Dr. Neha Verma',            'Teacher', 5, 55000.00),
    (106, 'Vikram Malhotra',           'Clerk',   3, 32000.00)
ON DUPLICATE KEY UPDATE 
    name = VALUES(name),
    type = VALUES(type),
    dept_id = VALUES(dept_id),
    basic_salary = VALUES(basic_salary);

-- Insert Demo Salary Records (Past 2 Months: September & October 2026)
-- Manager (emp_id 101): Basic=75000, HRA=15000, DA=22500, Gross=112500, PF=9000, Tax=11250, Net=92250
-- Clerk   (emp_id 102): Basic=35000, HRA=5250,  DA=7000,  Gross=47250,  PF=3500, Tax=2363,  Net=41387
-- Teacher (emp_id 103): Basic=50000, HRA=9000,  DA=12500, Gross=71500,  PF=6000, Tax=5720,  Net=59780
-- Manager (emp_id 104): Basic=70000, HRA=14000, DA=21000, Gross=105000, PF=8400, Tax=10500, Net=86100
-- Teacher (emp_id 105): Basic=55000, HRA=9900,  DA=13750, Gross=78650,  PF=6600, Tax=6292,  Net=65758
-- Clerk   (emp_id 106): Basic=32000, HRA=4800,  DA=6400,  Gross=43200,  PF=3200, Tax=2160,  Net=37840

INSERT INTO salary (salary_id, emp_id, month, year, basic, hra, da, gross, pf, tax, net_salary) VALUES
    (1, 101, 'September', 2026, 75000.00, 15000.00, 22500.00, 112500.00, 9000.00, 11250.00, 92250.00),
    (2, 102, 'September', 2026, 35000.00,  5250.00,  7000.00,  47250.00, 3500.00,  2363.00, 41387.00),
    (3, 103, 'September', 2026, 50000.00,  9000.00, 12500.00,  71500.00, 6000.00,  5720.00, 59780.00),
    (4, 104, 'September', 2026, 70000.00, 14000.00, 21000.00, 105000.00, 8400.00, 10500.00, 86100.00),
    (5, 105, 'September', 2026, 55000.00,  9900.00, 13750.00,  78650.00, 6600.00,  6292.00, 65758.00),
    (6, 106, 'September', 2026, 32000.00,  4800.00,  6400.00,  43200.00, 3200.00,  2160.00, 37840.00),
    (7, 101, 'October',   2026, 75000.00, 15000.00, 22500.00, 112500.00, 9000.00, 11250.00, 92250.00),
    (8, 102, 'October',   2026, 35000.00,  5250.00,  7000.00,  47250.00, 3500.00,  2363.00, 41387.00),
    (9, 103, 'October',   2026, 50000.00,  9000.00, 12500.00,  71500.00, 6000.00,  5720.00, 59780.00),
    (10,104, 'October',   2026, 70000.00, 14000.00, 21000.00, 105000.00, 8400.00, 10500.00, 86100.00),
    (11,105, 'October',   2026, 55000.00,  9900.00, 13750.00,  78650.00, 6600.00,  6292.00, 65758.00),
    (12,106, 'October',   2026, 32000.00,  4800.00,  6400.00,  43200.00, 3200.00,  2160.00, 37840.00)
ON DUPLICATE KEY UPDATE 
    net_salary = VALUES(net_salary);
