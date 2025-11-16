package com.mediaupload.model.dto;

import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for media upload response.
 * Contains all information about the uploaded media including QR code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaUploadResponse {

    private String uniqueId;
    private String originalFilename;
    private MediaType mediaType;
    private QualityLevel qualityLevel;
    private Long fileSize;
    private Long compressedFileSize;
    private Double compressionRatio;
    private String qrCodeData;
    private String accessUrl;
    private Integer width;
    private Integer height;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String message;

    /**
     * Create a response from the uploaded media entity.
     */
    public static MediaUploadResponse fromEntity(com.mediaupload.model.entity.MediaEntity entity, String qrCodeData) {
        return MediaUploadResponse.builder()
                .uniqueId(entity.getUniqueId())
                .originalFilename(entity.getOriginalFilename())
                .mediaType(entity.getMediaType())
                .qualityLevel(entity.getQualityLevel())
                .fileSize(entity.getFileSize())
                .compressedFileSize(entity.getCompressedFileSize())
                .compressionRatio(entity.getCompressionRatio())
                .qrCodeData(qrCodeData)
                .accessUrl(entity.getAccessUrl())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .durationSeconds(entity.getDurationSeconds())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .message("Media uploaded successfully")
                .build();
    }
}
