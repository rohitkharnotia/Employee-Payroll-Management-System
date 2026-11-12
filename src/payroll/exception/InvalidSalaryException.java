package payroll.exception;

/**
 * Custom checked exception thrown when an invalid salary value is encountered.
 * A salary is considered invalid when it is less than or equal to zero.
 */
public class InvalidSalaryException extends Exception {

    /**
     * Constructs a new InvalidSalaryException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidSalaryException(String message) {
        super(message);
    }
}
