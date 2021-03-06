package oop.ex6.exceptions;

public class InvalidMethodCallException extends SjavaException {
    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error: Invalid method call";
    //    private static final String METHOD_DECLARED_ALREADY_MESSAGE = "Error: A method declared more then
    // once";
    public static final String UNDECLARED_METHOD_ERROR = "Error: Call undeclared Method";
    public static final String CALL_METHOD_PARAMETERS_ERROR =
            "Error: Declared method number of parameters wrong";


    public InvalidMethodCallException() {
        super();
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public InvalidMethodCallException(String message) {
        super(message);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public InvalidMethodCallException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public InvalidMethodCallException(Throwable cause) {
        super(cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }
}
