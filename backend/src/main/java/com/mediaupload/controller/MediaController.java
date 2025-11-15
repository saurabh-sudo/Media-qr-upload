package com.mediaupload.controller;

import com.mediaupload.model.dto.MediaRetrievalResponse;
import com.mediaupload.model.dto.MediaUploadRequest;
import com.mediaupload.model.dto.MediaUploadResponse;
import com.mediaupload.model.enums.QualityLevel;
import com.mediaupload.service.MediaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for media operations.
 * Handles upload, retrieval, and deletion of media files.
 */
@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Upload a media file.
     *
     * @param file the file to upload
     * @param qualityLevel the quality level (HIGH, MEDIUM, LOW)
     * @param expirationDays optional expiration in days
     * @return the upload response with QR code
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaUploadResponse> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "qualityLevel", defaultValue = "MEDIUM") String qualityLevel,
            @RequestParam(value = "expirationDays", required = false) Integer expirationDays) {

        logger.info("Received upload request: filename={}, quality={}, expiration={}",
                file.getOriginalFilename(), qualityLevel, expirationDays);

        MediaUploadRequest request = MediaUploadRequest.builder()
                .qualityLevel(QualityLevel.fromString(qualityLevel))
                .expirationDays(expirationDays)
                .build();

        MediaUploadResponse response = mediaService.uploadMedia(file, request);

        logger.info("Upload successful: uniqueId={}", response.getUniqueId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get media information by unique ID.
     *
     * @param uniqueId the unique identifier
     * @return the media information
     */
    @GetMapping("/{uniqueId}")
    public ResponseEntity<MediaRetrievalResponse> getMedia(@PathVariable String uniqueId) {
        logger.debug("Retrieving media: uniqueId={}", uniqueId);

        MediaRetrievalResponse response = mediaService.getMedia(uniqueId);

        // Increment view count asynchronously
        try {
            mediaService.incrementViewCount(uniqueId);
        } catch (Exception e) {
            logger.warn("Failed to increment view count for uniqueId={}: {}", uniqueId, e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Delete media by unique ID.
     *
     * @param uniqueId the unique identifier
     * @return no content response
     */
    @DeleteMapping("/{uniqueId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String uniqueId) {
        logger.info("Deleting media: uniqueId={}", uniqueId);

        mediaService.deleteMedia(uniqueId);

        logger.info("Media deleted successfully: uniqueId={}", uniqueId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check endpoint for the media controller.
     *
     * @return OK status
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Media service is running");
    }
}
