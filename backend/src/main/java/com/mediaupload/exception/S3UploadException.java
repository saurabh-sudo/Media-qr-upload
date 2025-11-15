package com.mediaupload.exception;

/**
 * Exception thrown when S3 upload operations fail.
 */
public class S3UploadException extends MediaUploadException {

    public S3UploadException(String message) {
        super(message);
    }

    public S3UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
