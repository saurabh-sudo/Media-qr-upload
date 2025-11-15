package com.mediaupload.exception;

/**
 * Exception thrown when file validation fails.
 */
public class InvalidFileException extends MediaUploadException {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
