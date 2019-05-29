package oop.ex6.exceptions;


public class CodeLineException extends SjavaException {
    private final static String CODE_LINE_ERROR_NUM = "1";
    private static final String DEFAULT_PROBLEM_MESSAGE = "Error:Not a valid code line";

    public CodeLineException() {
        super();
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public CodeLineException(String message) {
        super(message);

        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public CodeLineException(String message, Throwable cause) {
        super(message, cause);

        problemNumber = CODE_LINE_ERROR_NUM;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public CodeLineException(Throwable cause) {
        super(cause);

        problemNumber = CODE_LINE_ERROR_NUM;
    }
}
