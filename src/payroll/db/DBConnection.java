package payroll.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton database connection manager for the Payroll system.
 * Provides a reusable JDBC connection to the MySQL payroll_db database
 * and automatically initializes schema & demo data if not already present.
 */
public class DBConnection {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";
    private static final String URL = "jdbc:mysql://localhost:3306/payroll_db?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change this to your MySQL password if set

    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DBConnection() {
    }

    /**
     * Returns a singleton Connection to the payroll_db database.
     * Creates a new connection if one does not exist or has been closed.
     *
     * @return a java.sql.Connection to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Include mysql-connector-j jar in classpath.", e);
        }
        return connection;
    }

    /**
     * Automatically ensures database tables exist and seeds demo data
     * (departments, employees, salaries) if the tables are empty.
     */
    public static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Step 1: Connect to server and create database if not present
            try (Connection sConn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
                 Statement sStmt = sConn.createStatement()) {
                sStmt.executeUpdate("CREATE DATABASE IF NOT EXISTS payroll_db");
            }

            // Step 2: Connect to payroll_db and create tables
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                // Table 1: departments
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS departments (" +
                    "  dept_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "  dept_name VARCHAR(100) NOT NULL UNIQUE" +
                    ")"
                );

                // Table 2: employees
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS employees (" +
                    "  emp_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "  name VARCHAR(100) NOT NULL," +
                    "  type VARCHAR(20) NOT NULL CHECK (type IN ('Manager', 'Clerk', 'Teacher'))," +
                    "  dept_id INT," +
                    "  basic_salary DOUBLE NOT NULL," +
                    "  FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL" +
                    ")"
                );

                // Table 3: salary
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS salary (" +
                    "  salary_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "  emp_id INT NOT NULL," +
                    "  month VARCHAR(20) NOT NULL," +
                    "  year INT NOT NULL," +
                    "  basic DOUBLE NOT NULL," +
                    "  hra DOUBLE NOT NULL," +
                    "  da DOUBLE NOT NULL," +
                    "  gross DOUBLE NOT NULL," +
                    "  pf DOUBLE NOT NULL," +
                    "  tax DOUBLE NOT NULL," +
                    "  net_salary DOUBLE NOT NULL," +
                    "  FOREIGN KEY (emp_id) REFERENCES employees(emp_id) ON DELETE CASCADE" +
                    ")"
                );

                // Seed Departments if empty
                ResultSet rsDept = stmt.executeQuery("SELECT COUNT(*) FROM departments");
                if (rsDept.next() && rsDept.getInt(1) == 0) {
                    stmt.executeUpdate(
                        "INSERT INTO departments (dept_id, dept_name) VALUES " +
                        "(1, 'Information Technology'), " +
                        "(2, 'Finance & Accounts'), " +
                        "(3, 'Human Resources'), " +
                        "(4, 'Administration'), " +
                        "(5, 'Academics')"
                    );
                    System.out.println("[INFO] Demo departments seeded.");
                }
                rsDept.close();

                // Seed Employees if empty
                ResultSet rsEmp = stmt.executeQuery("SELECT COUNT(*) FROM employees");
                if (rsEmp.next() && rsEmp.getInt(1) == 0) {
                    stmt.executeUpdate(
                        "INSERT INTO employees (emp_id, name, type, dept_id, basic_salary) VALUES " +
                        "(101, 'Rohit Kharnotia (Leader)', 'Manager', 1, 75000.00), " +
                        "(102, 'Sankeit Sharma', 'Clerk', 2, 35000.00), " +
                        "(103, 'Aryan', 'Teacher', 5, 50000.00), " +
                        "(104, 'Gaganpreet Singh', 'Manager', 4, 70000.00), " +
                        "(105, 'Dr. Neha Verma', 'Teacher', 5, 55000.00), " +
                        "(106, 'Vikram Malhotra', 'Clerk', 3, 32000.00)"
                    );
                    System.out.println("[INFO] Demo employees seeded.");
                }
                rsEmp.close();

                // Seed Salary Records if empty
                ResultSet rsSal = stmt.executeQuery("SELECT COUNT(*) FROM salary");
                if (rsSal.next() && rsSal.getInt(1) == 0) {
                    stmt.executeUpdate(
                        "INSERT INTO salary (emp_id, month, year, basic, hra, da, gross, pf, tax, net_salary) VALUES " +
                        "(101, 'September', 2026, 75000, 15000, 22500, 112500, 9000, 11250, 92250), " +
                        "(102, 'September', 2026, 35000,  5250,  7000,  47250, 3500,  2363, 41387), " +
                        "(103, 'September', 2026, 50000,  9000, 12500,  71500, 6000,  5720, 59780), " +
                        "(104, 'September', 2026, 70000, 14000, 21000, 105000, 8400, 10500, 86100), " +
                        "(105, 'September', 2026, 55000,  9900, 13750,  78650, 6600,  6292, 65758), " +
                        "(106, 'September', 2026, 32000,  4800,  6400,  43200, 3200,  2160, 37840), " +
                        "(101, 'October',   2026, 75000, 15000, 22500, 112500, 9000, 11250, 92250), " +
                        "(102, 'October',   2026, 35000,  5250,  7000,  47250, 3500,  2363, 41387), " +
                        "(103, 'October',   2026, 50000,  9000, 12500,  71500, 6000,  5720, 59780), " +
                        "(104, 'October',   2026, 70000, 14000, 21000, 105000, 8400, 10500, 86100), " +
                        "(105, 'October',   2026, 55000,  9900, 13750,  78650, 6600,  6292, 65758), " +
                        "(106, 'October',   2026, 32000,  4800,  6400,  43200, 3200,  2160, 37840)"
                    );
                    System.out.println("[INFO] Demo salary records seeded.");
                }
                rsSal.close();
            }
        } catch (Exception e) {
            System.err.println("[NOTE] Automatic database initialization skipped: " + e.getMessage());
        }
    }

    /**
     * Closes the current database connection if it is open.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
