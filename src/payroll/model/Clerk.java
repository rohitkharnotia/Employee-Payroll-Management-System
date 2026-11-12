package payroll.model;

import payroll.exception.InvalidSalaryException;

/**
 * Represents a Clerk employee.
 * Salary components: HRA=15%, DA=20%, PF=10% of basic; Tax=5% of gross.
 */
public class Clerk extends Employee {

    /**
     * Default no-arg constructor.
     */
    public Clerk() {
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
    public Clerk(int empId, String name, int deptId, String deptName,
                 double basicSalary) throws InvalidSalaryException {
        super(empId, name, "Clerk", deptId, deptName, basicSalary);
    }

    /**
     * Calculates the net salary for a Clerk.
     * HRA  = 15% of basic
     * DA   = 20% of basic
     * Gross = basic + HRA + DA
     * PF   = 10% of basic
     * Tax  = 5% of gross
     * Net  = gross - PF - Tax
     *
     * @return net salary rounded using Math.round()
     */
    @Override
    public double calculateSalary() {
        double basic = getBasicSalary();
        double hra   = basic * 0.15;
        double da    = basic * 0.20;
        double gross = basic + hra + da;
        double pf    = basic * 0.10;
        double tax   = gross * 0.05;
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
