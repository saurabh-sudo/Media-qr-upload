package com.mediaupload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for AWS S3 operations.
 */
public interface S3Service {

    /**
     * Upload a file to S3.
     *
     * @param file the file to upload
     * @param key the S3 object key
     * @param contentType the content type
     * @return the S3 URL of the uploaded file
     */
    String uploadFile(byte[] file, String key, String contentType);

    /**
     * Upload a multipart file to S3.
     *
     * @param file the multipart file
     * @param key the S3 object key
     * @return the S3 URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String key);

    /**
     * Delete a file from S3.
     *
     * @param key the S3 object key
     */
    void deleteFile(String key);

    /**
     * Generate a pre-signed URL for temporary access.
     *
     * @param key the S3 object key
     * @param durationMinutes duration in minutes
     * @return the pre-signed URL
     */
    String generatePresignedUrl(String key, int durationMinutes);

    /**
     * Check if a file exists in S3.
     *
     * @param key the S3 object key
     * @return true if exists, false otherwise
     */
    boolean fileExists(String key);

    /**
     * Get file size from S3.
     *
     * @param key the S3 object key
     * @return file size in bytes
     */
    long getFileSize(String key);
}
