package payroll.model;

import payroll.exception.InvalidSalaryException;

/**
 * Represents a Teacher employee.
 * Salary components: HRA=18%, DA=25%, PF=12% of basic; Tax=8% of gross.
 */
public class Teacher extends Employee {

    /**
     * Default no-arg constructor.
     */
    public Teacher() {
        super();
    }

    /**
     * Parameterized constructor.
     *
     * @param empId       the employee ID
     * @param name        the employee name
     * @param deptId      the department ID
     * @param deptName    the department name
     * @param basicSalary the basic salary
     * @throws InvalidSalaryException if basicSalary &lt;= 0
     */
    public Teacher(int empId, String name, int deptId, String deptName,
                   double basicSalary) throws InvalidSalaryException {
        super(empId, name, "Teacher", deptId, deptName, basicSalary);
    }

    /**
     * Calculates the net salary for a Teacher.
     * HRA  = 18% of basic
     * DA   = 25% of basic
     * Gross = basic + HRA + DA
     * PF   = 12% of basic
     * Tax  = 8% of gross
     * Net  = gross - PF - Tax
     *
     * @return net salary rounded using Math.round()
     */
    @Override
    public double calculateSalary() {
        double basic = getBasicSalary();
        double hra   = basic * 0.18;
        double da    = basic * 0.25;
        double gross = basic + hra + da;
        double pf    = basic * 0.12;
        double tax   = gross * 0.08;
        double net   = gross - pf - tax;

        net = Math.round(net);

        // Store breakdown for getSalaryBreakdown() / generatePaySlip()
        breakdownComponents[0] = basic;
        breakdownComponents[1] = hra;
        breakdownComponents[2] = da;
        breakdownComponents[3] = gross;
        breakdownComponents[4] = pf;
        breakdownComponents[5] = tax;
        breakdownComponents[6] = net;

        return net;
    }
}
