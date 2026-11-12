package payroll.model;

/**
 * Interface representing an entity that can calculate salary and generate a pay slip.
 */
public interface Payable {

    /**
     * Calculates and returns the net salary.
     *
     * @return the net salary amount
     */
    double calculateSalary();

    /**
     * Generates and returns a formatted pay slip string.
     *
     * @return a multi-line pay slip string
     */
    String generatePaySlip();
}
