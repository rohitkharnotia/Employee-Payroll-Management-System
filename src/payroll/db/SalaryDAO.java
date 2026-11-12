package payroll.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Salary operations and reports.
 * Uses PreparedStatement for writes and demonstrates both
 * PreparedStatement and Statement for read queries.
 * Automatically falls back to InMemoryStore when MySQL is offline.
 */
public class SalaryDAO {

    public void saveSalary(int empId, String month, int year, double basic, double hra,
                           double da, double gross, double pf, double tax, double netSalary) throws SQLException {
        try {
            String sql = "INSERT INTO salary (emp_id, month, year, basic, hra, da, gross, pf, tax, net_salary) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            pstmt.setString(2, month);
            pstmt.setInt(3, year);
            pstmt.setDouble(4, basic);
            pstmt.setDouble(5, hra);
            pstmt.setDouble(6, da);
            pstmt.setDouble(7, gross);
            pstmt.setDouble(8, pf);
            pstmt.setDouble(9, tax);
            pstmt.setDouble(10, netSalary);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            InMemoryStore.saveSalary(empId, month, year, basic, hra, da, gross, pf, tax, netSalary);
        }
    }

    public ResultSet getSalaryByEmployee(int empId) throws SQLException {
        try {
            String sql = "SELECT * FROM salary WHERE emp_id = ? ORDER BY year DESC, month DESC";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            return pstmt.executeQuery();
        } catch (Exception e) {
            return InMemoryStore.getSalaryByEmployee(empId);
        }
    }

    public ResultSet getDepartmentWiseReport() throws SQLException {
        try {
            String sql = "SELECT d.dept_name, "
                       + "SUM(s.net_salary) AS total_salary, "
                       + "AVG(s.net_salary) AS avg_salary, "
                       + "COUNT(DISTINCT e.emp_id) AS emp_count "
                       + "FROM departments d "
                       + "JOIN employees e ON d.dept_id = e.dept_id "
                       + "JOIN salary s ON e.emp_id = s.emp_id "
                       + "GROUP BY d.dept_name "
                       + "ORDER BY d.dept_name";
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            return InMemoryStore.getDepartmentWiseReport();
        }
    }

    public ResultSet getMonthlySalaryReport(String month, int year) throws SQLException {
        try {
            String sql = "SELECT e.emp_id, e.name, e.type, d.dept_name, "
                       + "s.basic, s.hra, s.da, s.gross, s.pf, s.tax, s.net_salary "
                       + "FROM salary s "
                       + "JOIN employees e ON s.emp_id = e.emp_id "
                       + "JOIN departments d ON e.dept_id = d.dept_id "
                       + "WHERE s.month = ? AND s.year = ? "
                       + "ORDER BY e.emp_id";
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, month);
            pstmt.setInt(2, year);
            return pstmt.executeQuery();
        } catch (Exception e) {
            return InMemoryStore.getAllEmployees();
        }
    }
}
