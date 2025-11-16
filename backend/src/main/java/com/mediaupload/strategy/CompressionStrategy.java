package com.mediaupload.strategy;

import com.mediaupload.model.enums.QualityLevel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Strategy interface for media compression.
 * Implements the Strategy design pattern for different compression levels.
 */
public interface CompressionStrategy {

    /**
     * Compress the given media file according to the strategy.
     *
     * @param file the file to compress
     * @return compressed file as byte array
     * @throws IOException if compression fails
     */
    byte[] compress(MultipartFile file) throws IOException;

    /**
     * Get the quality level this strategy implements.
     *
     * @return the quality level
     */
    QualityLevel getQualityLevel();

    /**
     * Check if this strategy supports the given content type.
     *
     * @param contentType the MIME type to check
     * @return true if supported, false otherwise
     */
    boolean supports(String contentType);
}
