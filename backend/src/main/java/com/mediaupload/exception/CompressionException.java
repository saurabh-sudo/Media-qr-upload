package com.mediaupload.exception;

/**
 * Exception thrown when media compression fails.
 */
public class CompressionException extends MediaUploadException {

    public CompressionException(String message) {
        super(message);
    }

    public CompressionException(String message, Throwable cause) {
        super(message, cause);
    }
}
