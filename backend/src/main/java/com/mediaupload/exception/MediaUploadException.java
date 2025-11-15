package com.mediaupload.exception;

/**
 * Base exception for all media upload related errors.
 */
public class MediaUploadException extends RuntimeException {

    public MediaUploadException(String message) {
        super(message);
    }

    public MediaUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
