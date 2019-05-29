package oop.ex6.exceptions;

public class InvalidIfOrWhileException extends SjavaException {
    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error: Not a valid if/while structure";
    public static final String STRING_OR_CHAR_IN_IF_WHILE = "Error: char/String type in while/if";

    public InvalidIfOrWhileException() {
        super();
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }


    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public InvalidIfOrWhileException(String message) {
        super(message);
        problemNumber = CODE_LINE_ERROR_NUM;
    }


    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public InvalidIfOrWhileException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }


    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public InvalidIfOrWhileException(Throwable cause) {
        super(cause);
        problemNumber = CODE_LINE_ERROR_NUM;
    }

}
