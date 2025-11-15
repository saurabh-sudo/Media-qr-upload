package com.mediaupload.model.entity;

import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing uploaded media files.
 * Stores metadata about uploaded images/videos including S3 location and QR code data.
 */
@Entity
@Table(name = "media_files", indexes = {
        @Index(name = "idx_unique_id", columnList = "unique_id", unique = true),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unique_id", nullable = false, unique = true, length = 36)
    private String uniqueId;

    @Column(name = "original_filename", nullable = false, length = 500)
    private String originalFilename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_level", nullable = false, length = 20)
    private QualityLevel qualityLevel;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "compressed_file_size")
    private Long compressedFileSize;

    @Column(name = "s3_bucket", nullable = false, length = 255)
    private String s3Bucket;

    @Column(name = "s3_key", nullable = false, length = 500)
    private String s3Key;

    @Column(name = "s3_url", nullable = false, length = 1000)
    private String s3Url;

    @Column(name = "qr_code_data", columnDefinition = "TEXT")
    private String qrCodeData;

    @Column(name = "access_url", nullable = false, length = 1000)
    private String accessUrl;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "thumbnail_s3_key", length = 500)
    private String thumbnailS3Key;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Increment the view count for this media.
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0L : this.viewCount) + 1;
    }

    /**
     * Check if the media has expired.
     *
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Calculate compression ratio.
     *
     * @return compression ratio as percentage, or 0 if not compressed
     */
    public double getCompressionRatio() {
        if (compressedFileSize == null || fileSize == null || fileSize == 0) {
            return 0.0;
        }
        return ((fileSize - compressedFileSize) / (double) fileSize) * 100.0;
    }
}
