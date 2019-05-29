package oop.ex6.exceptions;

public class SjavaIOException extends SjavaException {
    /**
     *
     */
    private final static String IO_ERROR_NUMBER = "2";
    private static final String DEFAULT_PROBLEM_MESSAGE = "IO PROBLEM";
    public static final String NO_FILE_PATH = "No file path in code argument";

    /**
     *
     */
    public SjavaIOException() {
        problemMessage = DEFAULT_PROBLEM_MESSAGE;
        problemNumber = IO_ERROR_NUMBER;
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public SjavaIOException(String message) {
        super(message);
        problemNumber = IO_ERROR_NUMBER;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public SjavaIOException(String message, Throwable cause) {
        super(message, cause);
        problemNumber = IO_ERROR_NUMBER;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public SjavaIOException(Throwable cause) {
        super(cause);
        problemNumber = IO_ERROR_NUMBER;
    }


}