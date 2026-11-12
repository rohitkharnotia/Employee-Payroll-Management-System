package payroll.model;

import payroll.exception.InvalidSalaryException;

/**
 * Abstract base class representing an employee in the payroll system.
 * Implements the {@link Payable} interface.
 */
public abstract class Employee implements Payable {

    private int empId;
    private String name;
    private String type;
    private int deptId;
    private String deptName;
    private double basicSalary;

    /**
     * Default no-arg constructor.
     */
    public Employee() {
    }

    /**
     * Parameterized constructor.
     *
     * @param empId       the employee ID
     * @param name        the employee name
     * @param type        the employee type (e.g., Manager, Clerk, Teacher)
     * @param deptId      the department ID
     * @param deptName    the department name
     * @param basicSalary the basic salary (must be &gt; 0)
     * @throws InvalidSalaryException if basicSalary is &lt;= 0
     */
    public Employee(int empId, String name, String type, int deptId,
                    String deptName, double basicSalary) throws InvalidSalaryException {
        this.empId = empId;
        this.name = name;
        this.type = type;
        this.deptId = deptId;
        this.deptName = deptName;
        setBasicSalary(basicSalary); // validate through setter
    }

    // -------------------- Getters & Setters --------------------

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    /**
     * Sets the basic salary after validation.
     *
     * @param basicSalary the basic salary to set (must be &gt; 0)
     * @throws InvalidSalaryException if basicSalary is &lt;= 0
     */
    public void setBasicSalary(double basicSalary) throws InvalidSalaryException {
        if (basicSalary <= 0) {
            throw new InvalidSalaryException(
                "Basic salary must be greater than 0. Provided: " + basicSalary);
        }
        this.basicSalary = basicSalary;
    }

    // -------------------- Abstract --------------------

    /**
     * Calculates the net salary. Must be implemented by subclasses.
     *
     * @return the net salary
     */
    @Override
    public abstract double calculateSalary();

    // -------------------- Salary Breakdown --------------------

    /**
     * Returns the salary breakdown as an array:
     * [basic, hra, da, gross, pf, tax, net].
     * Relies on the subclass's {@link #calculateSalary()} to trigger computation.
     * Subclasses set HRA/DA/PF/Tax percentages; this method computes using
     * the same logic by calling {@link #calculateSalary()} first.
     *
     * @return double array of length 7
     */
    public double[] getSalaryBreakdown() {
        double net = calculateSalary();

        // Reconstruct components using subclass-specific percentages.
        // Since we cannot know the percentages here, we use a simpler approach:
        // the subclass calculateSalary() returns the net. We store breakdown
        // data via a protected helper.
        return getBreakdownComponents();
    }

    /**
     * Protected helper that subclasses call to supply breakdown components.
     * Each subclass stores its last computed breakdown in this array.
     */
    protected double[] breakdownComponents = new double[7];

    /**
     * Returns the cached breakdown components.
     */
    protected double[] getBreakdownComponents() {
        return breakdownComponents;
    }

    // -------------------- Pay Slip --------------------

    /**
     * Generates a formatted multi-line pay slip string.
     *
     * @return the pay slip string
     */
    @Override
    public String generatePaySlip() {
        double[] breakdown = getSalaryBreakdown();
        double basic = Math.round(breakdown[0]);
        double hra   = Math.round(breakdown[1]);
        double da    = Math.round(breakdown[2]);
        double gross = Math.round(breakdown[3]);
        double pf    = Math.round(breakdown[4]);
        double tax   = Math.round(breakdown[5]);
        double net   = Math.round(breakdown[6]);

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("           EMPLOYEE PAY SLIP            \n");
        sb.append("========================================\n");
        sb.append(String.format("Employee ID   : %d%n", empId));
        sb.append(String.format("Name          : %s%n", name));
        sb.append(String.format("Type          : %s%n", type));
        sb.append(String.format("Department    : %s (ID: %d)%n", deptName, deptId));
        sb.append("----------------------------------------\n");
        sb.append(String.format("Basic Salary  : %.2f%n", basic));
        sb.append(String.format("HRA           : %.2f%n", hra));
        sb.append(String.format("DA            : %.2f%n", da));
        sb.append(String.format("Gross Salary  : %.2f%n", gross));
        sb.append("----------------------------------------\n");
        sb.append(String.format("PF Deduction  : %.2f%n", pf));
        sb.append(String.format("Tax Deduction : %.2f%n", tax));
        sb.append("----------------------------------------\n");
        sb.append(String.format("Net Salary    : %.2f%n", net));
        sb.append("========================================\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Employee{empId=" + empId + ", name='" + name + "', type='" + type
                + "', deptId=" + deptId + ", deptName='" + deptName
                + "', basicSalary=" + basicSalary + "}";
    }
}
