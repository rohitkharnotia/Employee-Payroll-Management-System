package payroll.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Employee CRUD operations.
 * Uses PreparedStatement for write operations and Statement/PreparedStatement for reads.
 * Automatically falls back to InMemoryStore when MySQL is offline.
 */
public class EmployeeDAO {

    public void addEmployee(String name, String type, int deptId, double basicSalary) throws SQLException {
        try {
            String sql = "INSERT INTO employees (name, type, dept_id, basic_salary) VALUES (?, ?, ?, ?)";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, deptId);
            pstmt.setDouble(4, basicSalary);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            InMemoryStore.addEmployee(name, type, deptId, basicSalary);
        }
    }

    public ResultSet getAllEmployees() throws SQLException {
        try {
            String sql = "SELECT e.emp_id, e.name, e.type, e.dept_id, d.dept_name, e.basic_salary "
                       + "FROM employees e "
                       + "JOIN departments d ON e.dept_id = d.dept_id "
                       + "ORDER BY e.emp_id";
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            return InMemoryStore.getAllEmployees();
        }
    }

    public ResultSet getEmployeeById(int empId) throws SQLException {
        try {
            String sql = "SELECT e.emp_id, e.name, e.type, e.dept_id, d.dept_name, e.basic_salary "
                       + "FROM employees e "
                       + "JOIN departments d ON e.dept_id = d.dept_id "
                       + "WHERE e.emp_id = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            return pstmt.executeQuery();
        } catch (Exception e) {
            return InMemoryStore.getEmployeeById(empId);
        }
    }

    public void updateEmployee(int empId, String name, String type, int deptId, double basicSalary) throws SQLException {
        try {
            String sql = "UPDATE employees SET name = ?, type = ?, dept_id = ?, basic_salary = ? WHERE emp_id = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, deptId);
            pstmt.setDouble(4, basicSalary);
            pstmt.setInt(5, empId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            InMemoryStore.updateEmployee(empId, name, type, deptId, basicSalary);
        }
    }

    public void deleteEmployee(int empId) throws SQLException {
        try {
            String sql = "DELETE FROM employees WHERE emp_id = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            InMemoryStore.deleteEmployee(empId);
        }
    }

    public ResultSet searchEmployees(String keyword) throws SQLException {
        try {
            String sql = "SELECT e.emp_id, e.name, e.type, e.dept_id, d.dept_name, e.basic_salary "
                       + "FROM employees e "
                       + "JOIN departments d ON e.dept_id = d.dept_id "
                       + "WHERE e.name LIKE ? "
                       + "ORDER BY e.emp_id";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            return pstmt.executeQuery();
        } catch (Exception e) {
            return InMemoryStore.searchEmployees(keyword);
        }
    }
}
