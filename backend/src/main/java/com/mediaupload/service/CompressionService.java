package com.mediaupload.service;

import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for media compression.
 */
public interface CompressionService {

    /**
     * Compress a media file based on quality level and media type.
     *
     * @param file the file to compress
     * @param qualityLevel the desired quality level
     * @param mediaType the type of media
     * @return compressed file as byte array
     */
    byte[] compressMedia(MultipartFile file, QualityLevel qualityLevel, MediaType mediaType);

    /**
     * Check if compression is supported for the given media type.
     *
     * @param mediaType the media type
     * @return true if supported, false otherwise
     */
    boolean supportsCompression(MediaType mediaType);
}
