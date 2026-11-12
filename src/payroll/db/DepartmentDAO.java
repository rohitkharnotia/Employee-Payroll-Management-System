package payroll.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Department operations.
 * Connects to MySQL via DBConnection, with graceful automatic fallback
 * to InMemoryStore when MySQL is not connected.
 */
public class DepartmentDAO {

    public void addDepartment(String deptName) throws SQLException {
        try {
            String sql = "INSERT INTO departments (dept_name) VALUES (?)";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deptName);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            InMemoryStore.addDepartment(deptName);
        }
    }

    public ResultSet getAllDepartments() throws SQLException {
        try {
            String sql = "SELECT dept_id, dept_name FROM departments ORDER BY dept_id";
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            return InMemoryStore.getAllDepartments();
        }
    }

    public String getDepartmentName(int deptId) throws SQLException {
        try {
            String sql = "SELECT dept_name FROM departments WHERE dept_id = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptId);
            ResultSet rs = pstmt.executeQuery();
            String deptName = null;
            if (rs.next()) {
                deptName = rs.getString("dept_name");
            }
            rs.close();
            pstmt.close();
            if (deptName != null) return deptName;
        } catch (Exception e) {
            // fall through to InMemoryStore
        }
        return InMemoryStore.getDepartmentName(deptId);
    }
}
