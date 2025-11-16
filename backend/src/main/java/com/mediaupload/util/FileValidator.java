package com.mediaupload.util;

import com.mediaupload.exception.InvalidFileException;
import com.mediaupload.model.enums.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for validating uploaded files.
 * Implements comprehensive validation rules for media files.
 */
@Component
public class FileValidator {

    private static final Logger logger = LoggerFactory.getLogger(FileValidator.class);
    private static final long MIN_FILE_SIZE = 1024; // 1 KB minimum

    private final FileTypeDetector fileTypeDetector;

    public FileValidator(FileTypeDetector fileTypeDetector) {
        this.fileTypeDetector = fileTypeDetector;
    }

    /**
     * Validate uploaded file comprehensively.
     *
     * @param file the file to validate
     * @throws InvalidFileException if validation fails
     */
    public void validate(MultipartFile file) {
        validateNotNull(file);
        validateNotEmpty(file);
        validateFileSize(file);
        validateFileName(file);
    }

    /**
     * Validate file is not null.
     */
    private void validateNotNull(MultipartFile file) {
        if (file == null) {
            throw new InvalidFileException("No file provided");
        }
    }

    /**
     * Validate file is not empty.
     */
    private void validateNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }
    }

    /**
     * Validate file size against media type limits.
     */
    private void validateFileSize(MultipartFile file) {
        long fileSize = file.getSize();

        if (fileSize < MIN_FILE_SIZE) {
            throw new InvalidFileException(
                String.format("File is too small. Minimum size: %d bytes", MIN_FILE_SIZE)
            );
        }

        // Detect media type first
        MediaType mediaType = fileTypeDetector.detectAndValidateMediaType(file);
        long maxSize = mediaType.getMaxFileSize();

        if (fileSize > maxSize) {
            throw new InvalidFileException(
                String.format("File size (%d bytes) exceeds maximum allowed size for %s (%d bytes / %.2f MB)",
                        fileSize, mediaType.name(), maxSize, maxSize / (1024.0 * 1024.0))
            );
        }

        logger.debug("File size validation passed: {} bytes for type: {}", fileSize, mediaType);
    }

    /**
     * Validate filename is present and not malicious.
     */
    private void validateFileName(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename == null || filename.trim().isEmpty()) {
            throw new InvalidFileException("Filename is missing");
        }

        // Check for path traversal attempts
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            logger.warn("Potential path traversal attempt detected in filename: {}", filename);
            throw new InvalidFileException("Invalid filename: contains illegal characters");
        }

        // Check filename length
        if (filename.length() > 255) {
            throw new InvalidFileException("Filename is too long (max 255 characters)");
        }

        logger.debug("Filename validation passed: {}", filename);
    }

    /**
     * Sanitize filename by removing special characters and limiting length.
     *
     * @param filename the original filename
     * @return sanitized filename
     */
    public String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unnamed";
        }

        // Remove path components
        String sanitized = filename.replaceAll("[\\\\/]", "");

        // Replace special characters with underscores
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Limit length while preserving extension
        if (sanitized.length() > 100) {
            String extension = fileTypeDetector.getFileExtension(sanitized);
            int maxNameLength = 100 - extension.length() - 1;
            String name = sanitized.substring(0, Math.min(maxNameLength, sanitized.lastIndexOf('.')));
            sanitized = name + "." + extension;
        }

        return sanitized;
    }
}
