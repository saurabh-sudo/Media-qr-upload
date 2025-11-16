package com.mediaupload.service;

import com.mediaupload.model.dto.MediaRetrievalResponse;
import com.mediaupload.model.dto.MediaUploadRequest;
import com.mediaupload.model.dto.MediaUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for media operations.
 * Main service orchestrating file upload, compression, and QR code generation.
 */
public interface MediaService {

    /**
     * Upload a media file with the specified quality level.
     *
     * @param file the file to upload
     * @param request the upload request parameters
     * @return the upload response with QR code and access URL
     */
    MediaUploadResponse uploadMedia(MultipartFile file, MediaUploadRequest request);

    /**
     * Retrieve media information by unique ID.
     *
     * @param uniqueId the unique identifier
     * @return the media retrieval response
     */
    MediaRetrievalResponse getMedia(String uniqueId);

    /**
     * Delete media by unique ID.
     *
     * @param uniqueId the unique identifier
     */
    void deleteMedia(String uniqueId);

    /**
     * Increment view count for media.
     *
     * @param uniqueId the unique identifier
     */
    void incrementViewCount(String uniqueId);
}
