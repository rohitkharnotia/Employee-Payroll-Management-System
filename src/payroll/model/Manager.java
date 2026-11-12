package payroll.model;

import payroll.exception.InvalidSalaryException;

/**
 * Represents a Manager employee.
 * Salary components: HRA=20%, DA=30%, PF=12% of basic; Tax=10% of gross.
 */
public class Manager extends Employee {

    /**
     * Default no-arg constructor.
     */
    public Manager() {
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
    public Manager(int empId, String name, int deptId, String deptName,
                   double basicSalary) throws InvalidSalaryException {
        super(empId, name, "Manager", deptId, deptName, basicSalary);
    }

    /**
     * Calculates the net salary for a Manager.
     * HRA  = 20% of basic
     * DA   = 30% of basic
     * Gross = basic + HRA + DA
     * PF   = 12% of basic
     * Tax  = 10% of gross
     * Net  = gross - PF - Tax
     *
     * @return net salary rounded using Math.round()
     */
    @Override
    public double calculateSalary() {
        double basic = getBasicSalary();
        double hra   = basic * 0.20;
        double da    = basic * 0.30;
        double gross = basic + hra + da;
        double pf    = basic * 0.12;
        double tax   = gross * 0.10;
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
