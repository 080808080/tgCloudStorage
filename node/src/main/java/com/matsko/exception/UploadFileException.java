package com.matsko.exception;

/**
 * This one is a custom exception inherited from {@link RuntimeException}.
 */
public class UploadFileException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message text reporting an exception to a user.
     * @param cause reason for the exception.
     */
    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message text reporting an exception to a user.
     */
    public UploadFileException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail cause.
     *
     * @param cause reason for the exception.
     */
    public UploadFileException(Throwable cause) {
        super(cause);
    }
}
