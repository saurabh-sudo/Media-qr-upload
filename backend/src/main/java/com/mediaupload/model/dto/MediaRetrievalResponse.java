package com.mediaupload.model.dto;

import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for media retrieval response.
 * Contains media metadata and access URLs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaRetrievalResponse {

    private String uniqueId;
    private String originalFilename;
    private MediaType mediaType;
    private QualityLevel qualityLevel;
    private String contentType;
    private Long fileSize;
    private String s3Url;
    private String accessUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Integer durationSeconds;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isExpired;

    /**
     * Create a response from a media entity.
     */
    public static MediaRetrievalResponse fromEntity(com.mediaupload.model.entity.MediaEntity entity) {
        return MediaRetrievalResponse.builder()
                .uniqueId(entity.getUniqueId())
                .originalFilename(entity.getOriginalFilename())
                .mediaType(entity.getMediaType())
                .qualityLevel(entity.getQualityLevel())
                .contentType(entity.getContentType())
                .fileSize(entity.getCompressedFileSize() != null ?
                          entity.getCompressedFileSize() : entity.getFileSize())
                .s3Url(entity.getS3Url())
                .accessUrl(entity.getAccessUrl())
                .thumbnailUrl(entity.getThumbnailS3Key())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .durationSeconds(entity.getDurationSeconds())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .isExpired(entity.isExpired())
                .build();
    }
}
