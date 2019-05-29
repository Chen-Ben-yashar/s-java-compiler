package oop.ex6.exceptions;

public class InvalidMethodException extends SjavaException {
    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error: Invalid method declaration";
    public static final String METHOD_DECLARED_ALREADY_MESSAGE = "Error: A method declared more then once";
    public static final String METHOD_DECLARED_INSIDE_SCOPE_MESSAGE = "Error: Declare method inside a scope";


    public InvalidMethodException() {
        super();
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public InvalidMethodException(String message) {
        super(message);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public InvalidMethodException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public InvalidMethodException(Throwable cause) {
        super(cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }
}
