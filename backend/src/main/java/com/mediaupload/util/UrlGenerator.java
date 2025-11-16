package com.mediaupload.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating URLs for media access.
 */
@Component
public class UrlGenerator {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    /**
     * Generate access URL for a media file.
     *
     * @param uniqueId the unique identifier of the media
     * @return the complete access URL
     */
    public String generateAccessUrl(String uniqueId) {
        return String.format("%s/view/%s", frontendUrl, uniqueId);
    }

    /**
     * Generate API URL for media retrieval.
     *
     * @param uniqueId the unique identifier of the media
     * @return the API endpoint URL
     */
    public String generateApiUrl(String uniqueId) {
        return String.format("%s/api/media/%s", baseUrl, uniqueId);
    }

    /**
     * Generate S3 object key for a media file.
     *
     * @param uniqueId the unique identifier
     * @param filename the original filename
     * @return the S3 object key
     */
    public String generateS3Key(String uniqueId, String filename) {
        String extension = filename.contains(".") ?
                filename.substring(filename.lastIndexOf(".")) : "";
        return String.format("media/%s/%s%s",
                java.time.LocalDate.now().toString(),
                uniqueId,
                extension);
    }

    /**
     * Generate S3 key for thumbnail.
     *
     * @param uniqueId the unique identifier
     * @return the thumbnail S3 key
     */
    public String generateThumbnailKey(String uniqueId) {
        return String.format("thumbnails/%s/%s.jpg",
                java.time.LocalDate.now().toString(),
                uniqueId);
    }
}
