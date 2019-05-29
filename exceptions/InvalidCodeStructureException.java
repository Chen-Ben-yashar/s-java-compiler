package oop.ex6.exceptions;

public class InvalidCodeStructureException extends SjavaException {
    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error: Not a valid code structure";
    public static final String OPEN_CLOSE_BRACKETS_NOT_EVEN = "Error: no equal open/close brackets in code";

    public InvalidCodeStructureException() {
        super();
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public InvalidCodeStructureException(String message) {
        super(message);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public InvalidCodeStructureException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public InvalidCodeStructureException(Throwable cause) {
        super(cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }
}
