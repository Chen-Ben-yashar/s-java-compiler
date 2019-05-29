package oop.ex6.exceptions;

public class VariableMismatchException extends SjavaException {

    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error: Variable Type or Value mismatch";
    public static final String ERROR_VARIABLE_IN_SCOPE = "Error: same variable already in scope";
    public static final String ERROR_VARIABLE_NOT_INITIALIZE = "Error: Not initialized final type";
    public static final String UNDECLARED_VARIABLE = "Error: Undeclared variable assignment";
    public static final String ERROR_VARIABLE_NOT_ASSIGNED = "Error: Variable is not assigned";


    public VariableMismatchException() {
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public VariableMismatchException(String message) {
        super(message);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public VariableMismatchException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public VariableMismatchException(Throwable cause) {
        super(cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }
}

