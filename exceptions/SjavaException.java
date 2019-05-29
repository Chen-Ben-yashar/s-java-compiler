package oop.ex6.exceptions;

public abstract class SjavaException extends Exception {
    protected static String problemMessage;
    protected static String problemNumber;


    public SjavaException() {
        super();
    }

    /**
     * Exception thrown with a String Message.
     *
     * @param message The exception message.
     */
    public SjavaException(String message) {
        super(message);
        problemMessage = message;
    }

    /**
     * Exception thrown with a String Message, and throwable cause.
     *
     * @param message String message.
     * @param cause   Throwable cause.
     */
    public SjavaException(String message, Throwable cause) {
        super(message, cause);
        problemMessage = message;
    }

    /**
     * Exception thrown with a throwable cause.
     *
     * @param cause Throwable cause.
     */
    public SjavaException(Throwable cause) {
        super(cause);
        problemMessage = cause.getMessage();
    }

    public String getMessage() {
        return (problemNumber + "\n" + problemMessage);
    }

}
