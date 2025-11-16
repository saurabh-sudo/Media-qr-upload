package com.mediaupload.exception;

/**
 * Exception thrown when requested media is not found.
 */
public class MediaNotFoundException extends MediaUploadException {

    public MediaNotFoundException(String uniqueId) {
        super(String.format("Media with ID '%s' not found", uniqueId));
    }

    public MediaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
