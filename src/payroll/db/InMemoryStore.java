package payroll.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory fallback data store populated with realistic demo records
 * (including Team Members: Rohit Kharnotia, Sankeit Sharma, Aryan, Gaganpreet Singh).
 * Ensures that if MySQL is offline or MySQL Connector is missing, the application
 * still functions 100% with full CRUD, search, calculation, and reporting.
 */
public class InMemoryStore {

    public static class EmpRow {
        public int empId;
        public String name;
        public String type;
        public int deptId;
        public double basicSalary;

        public EmpRow(int empId, String name, String type, int deptId, double basicSalary) {
            this.empId = empId;
            this.name = name;
            this.type = type;
            this.deptId = deptId;
            this.basicSalary = basicSalary;
        }
    }

    public static class DeptRow {
        public int deptId;
        public String deptName;

        public DeptRow(int deptId, String deptName) {
            this.deptId = deptId;
            this.deptName = deptName;
        }
    }

    public static class SalaryRow {
        public int salaryId;
        public int empId;
        public String month;
        public int year;
        public double basic;
        public double hra;
        public double da;
        public double gross;
        public double pf;
        public double tax;
        public double netSalary;

        public SalaryRow(int salaryId, int empId, String month, int year, double basic,
                         double hra, double da, double gross, double pf, double tax, double netSalary) {
            this.salaryId = salaryId;
            this.empId = empId;
            this.month = month;
            this.year = year;
            this.basic = basic;
            this.hra = hra;
            this.da = da;
            this.gross = gross;
            this.pf = pf;
            this.tax = tax;
            this.netSalary = netSalary;
        }
    }

    private static final List<DeptRow> departments = new ArrayList<>();
    private static final List<EmpRow> employees = new ArrayList<>();
    private static final List<SalaryRow> salaries = new ArrayList<>();
    private static int nextEmpId = 107;
    private static int nextSalaryId = 13;

    static {
        // 1. Seed Departments
        departments.add(new DeptRow(1, "Information Technology"));
        departments.add(new DeptRow(2, "Finance & Accounts"));
        departments.add(new DeptRow(3, "Human Resources"));
        departments.add(new DeptRow(4, "Administration"));
        departments.add(new DeptRow(5, "Academics"));

        // 2. Seed Employees with Team Members
        employees.add(new EmpRow(101, "Rohit Kharnotia (Leader)", "Manager", 1, 75000.00));
        employees.add(new EmpRow(102, "Sankeit Sharma", "Clerk", 2, 35000.00));
        employees.add(new EmpRow(103, "Aryan", "Teacher", 5, 50000.00));
        employees.add(new EmpRow(104, "Gaganpreet Singh", "Manager", 4, 70000.00));
        employees.add(new EmpRow(105, "Dr. Neha Verma", "Teacher", 5, 55000.00));
        employees.add(new EmpRow(106, "Vikram Malhotra", "Clerk", 3, 32000.00));

        // 3. Seed Salary Records (Past 2 months: September & October 2026)
        salaries.add(new SalaryRow(1, 101, "September", 2026, 75000, 15000, 22500, 112500, 9000, 11250, 92250));
        salaries.add(new SalaryRow(2, 102, "September", 2026, 35000, 5250, 7000, 47250, 3500, 2363, 41387));
        salaries.add(new SalaryRow(3, 103, "September", 2026, 50000, 9000, 12500, 71500, 6000, 5720, 59780));
        salaries.add(new SalaryRow(4, 104, "September", 2026, 70000, 14000, 21000, 105000, 8400, 10500, 86100));
        salaries.add(new SalaryRow(5, 105, "September", 2026, 55000, 9900, 13750, 78650, 6600, 6292, 65758));
        salaries.add(new SalaryRow(6, 106, "September", 2026, 32000, 4800, 6400, 43200, 3200, 2160, 37840));

        salaries.add(new SalaryRow(7, 101, "October", 2026, 75000, 15000, 22500, 112500, 9000, 11250, 92250));
        salaries.add(new SalaryRow(8, 102, "October", 2026, 35000, 5250, 7000, 47250, 3500, 2363, 41387));
        salaries.add(new SalaryRow(9, 103, "October", 2026, 50000, 9000, 12500, 71500, 6000, 5720, 59780));
        salaries.add(new SalaryRow(10, 104, "October", 2026, 70000, 14000, 21000, 105000, 8400, 10500, 86100));
        salaries.add(new SalaryRow(11, 105, "October", 2026, 55000, 9900, 13750, 78650, 6600, 6292, 65758));
        salaries.add(new SalaryRow(12, 106, "October", 2026, 32000, 4800, 6400, 43200, 3200, 2160, 37840));
    }

    // ----------------- Department Operations -----------------
    public static ResultSet getAllDepartments() {
        String[] cols = {"dept_id", "dept_name"};
        List<Object[]> rows = new ArrayList<>();
        for (DeptRow d : departments) {
            rows.add(new Object[]{d.deptId, d.deptName});
        }
        return MockResultSet.create(cols, rows);
    }

    public static String getDepartmentName(int deptId) {
        for (DeptRow d : departments) {
            if (d.deptId == deptId) return d.deptName;
        }
        return "General";
    }

    public static void addDepartment(String name) {
        int newId = departments.size() + 1;
        departments.add(new DeptRow(newId, name));
    }

    // ----------------- Employee Operations -----------------
    public static ResultSet getAllEmployees() {
        String[] cols = {"emp_id", "name", "type", "dept_id", "dept_name", "basic_salary"};
        List<Object[]> rows = new ArrayList<>();
        for (EmpRow e : employees) {
            rows.add(new Object[]{e.empId, e.name, e.type, e.deptId, getDepartmentName(e.deptId), e.basicSalary});
        }
        return MockResultSet.create(cols, rows);
    }

    public static ResultSet getEmployeeById(int empId) {
        String[] cols = {"emp_id", "name", "type", "dept_id", "dept_name", "basic_salary"};
        List<Object[]> rows = new ArrayList<>();
        for (EmpRow e : employees) {
            if (e.empId == empId) {
                rows.add(new Object[]{e.empId, e.name, e.type, e.deptId, getDepartmentName(e.deptId), e.basicSalary});
                break;
            }
        }
        return MockResultSet.create(cols, rows);
    }

    public static void addEmployee(String name, String type, int deptId, double basicSalary) {
        employees.add(new EmpRow(nextEmpId++, name, type, deptId, basicSalary));
    }

    public static void updateEmployee(int empId, String name, String type, int deptId, double basicSalary) {
        for (EmpRow e : employees) {
            if (e.empId == empId) {
                e.name = name;
                e.type = type;
                e.deptId = deptId;
                e.basicSalary = basicSalary;
                return;
            }
        }
    }

    public static void deleteEmployee(int empId) {
        employees.removeIf(e -> e.empId == empId);
    }

    public static ResultSet searchEmployees(String keyword) {
        String[] cols = {"emp_id", "name", "type", "dept_id", "dept_name", "basic_salary"};
        List<Object[]> rows = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (EmpRow e : employees) {
            if (e.name.toLowerCase().contains(lower)) {
                rows.add(new Object[]{e.empId, e.name, e.type, e.deptId, getDepartmentName(e.deptId), e.basicSalary});
            }
        }
        return MockResultSet.create(cols, rows);
    }

    // ----------------- Salary Operations -----------------
    public static void saveSalary(int empId, String month, int year, double basic, double hra,
                                  double da, double gross, double pf, double tax, double netSalary) {
        salaries.add(new SalaryRow(nextSalaryId++, empId, month, year, basic, hra, da, gross, pf, tax, netSalary));
    }

    public static ResultSet getSalaryByEmployee(int empId) {
        String[] cols = {"salary_id", "emp_id", "month", "year", "basic", "hra", "da", "gross", "pf", "tax", "net_salary"};
        List<Object[]> rows = new ArrayList<>();
        for (SalaryRow s : salaries) {
            if (s.empId == empId) {
                rows.add(new Object[]{s.salaryId, s.empId, s.month, s.year, s.basic, s.hra, s.da, s.gross, s.pf, s.tax, s.netSalary});
            }
        }
        return MockResultSet.create(cols, rows);
    }

    public static ResultSet getDepartmentWiseReport() {
        String[] cols = {"dept_name", "emp_count", "total_salary", "avg_salary"};
        List<Object[]> rows = new ArrayList<>();

        Map<Integer, List<Double>> deptSalaries = new HashMap<>();
        Map<Integer, Integer> deptEmpCount = new HashMap<>();

        for (DeptRow d : departments) {
            deptSalaries.put(d.deptId, new ArrayList<>());
            deptEmpCount.put(d.deptId, 0);
        }

        // Count unique employees per department
        for (EmpRow e : employees) {
            if (deptEmpCount.containsKey(e.deptId)) {
                deptEmpCount.put(e.deptId, deptEmpCount.get(e.deptId) + 1);
            }
        }

        // Aggregate salaries
        for (SalaryRow s : salaries) {
            for (EmpRow e : employees) {
                if (e.empId == s.empId && deptSalaries.containsKey(e.deptId)) {
                    deptSalaries.get(e.deptId).add(s.netSalary);
                    break;
                }
            }
        }

        for (DeptRow d : departments) {
            List<Double> list = deptSalaries.get(d.deptId);
            int empCount = deptEmpCount.getOrDefault(d.deptId, 0);
            double total = 0.0;
            for (double val : list) total += val;
            double avg = list.isEmpty() ? 0.0 : total / list.size();
            rows.add(new Object[]{d.deptName, empCount, total, avg});
        }

        return MockResultSet.create(cols, rows);
    }
}

