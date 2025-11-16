package com.mediaupload.util;

import com.mediaupload.exception.InvalidFileException;
import com.mediaupload.model.enums.MediaType;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Utility class for detecting file types using Apache Tika.
 * Provides robust MIME type detection beyond simple extension checking.
 */
@Component
public class FileTypeDetector {

    private static final Logger logger = LoggerFactory.getLogger(FileTypeDetector.class);
    private final Tika tika;

    public FileTypeDetector() {
        this.tika = new Tika();
    }

    /**
     * Detect the actual MIME type of a file using content analysis.
     *
     * @param file the file to analyze
     * @return the detected MIME type
     * @throws InvalidFileException if detection fails
     */
    public String detectMimeType(MultipartFile file) {
        try {
            String detectedType = tika.detect(file.getInputStream());
            logger.debug("Detected MIME type: {} for file: {}", detectedType, file.getOriginalFilename());
            return detectedType;
        } catch (IOException e) {
            logger.error("Failed to detect MIME type for file: {}", file.getOriginalFilename(), e);
            throw new InvalidFileException("Failed to detect file type", e);
        }
    }

    /**
     * Detect and validate media type.
     *
     * @param file the file to check
     * @return the MediaType enum
     * @throws InvalidFileException if file type is not supported
     */
    public MediaType detectAndValidateMediaType(MultipartFile file) {
        String mimeType = detectMimeType(file);
        MediaType mediaType = MediaType.fromMimeType(mimeType);

        if (mediaType == null) {
            logger.warn("Unsupported file type: {} for file: {}", mimeType, file.getOriginalFilename());
            throw new InvalidFileException(
                String.format("Unsupported file type: %s. Supported types: images (JPEG, PNG, GIF, WebP, BMP) and videos (MP4, MOV, AVI, WebM, MKV)",
                        mimeType)
            );
        }

        return mediaType;
    }

    /**
     * Get file extension from filename.
     *
     * @param filename the filename
     * @return the file extension (without dot)
     */
    public String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
