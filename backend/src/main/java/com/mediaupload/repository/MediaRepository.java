package com.mediaupload.repository;

import com.mediaupload.model.entity.MediaEntity;
import com.mediaupload.model.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MediaEntity database operations.
 * Provides CRUD operations and custom queries for media files.
 */
@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {

    /**
     * Find a media entity by its unique ID.
     *
     * @param uniqueId the unique identifier
     * @return Optional containing the media entity if found
     */
    Optional<MediaEntity> findByUniqueId(String uniqueId);

    /**
     * Find all active media files by media type.
     *
     * @param mediaType the media type to filter by
     * @return list of media entities
     */
    List<MediaEntity> findByMediaTypeAndIsActiveTrue(MediaType mediaType);

    /**
     * Find all expired media files.
     *
     * @param currentTime the current timestamp
     * @return list of expired media entities
     */
    @Query("SELECT m FROM MediaEntity m WHERE m.expiresAt IS NOT NULL AND m.expiresAt < :currentTime AND m.isActive = true")
    List<MediaEntity> findExpiredMedia(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Find media files created within a date range.
     *
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return list of media entities
     */
    @Query("SELECT m FROM MediaEntity m WHERE m.createdAt BETWEEN :startDate AND :endDate ORDER BY m.createdAt DESC")
    List<MediaEntity> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Count active media files by type.
     *
     * @param mediaType the media type
     * @return count of active media files
     */
    long countByMediaTypeAndIsActiveTrue(MediaType mediaType);

    /**
     * Find top N most viewed media files.
     *
     * @param limit the number of results to return
     * @return list of most viewed media entities
     */
    @Query("SELECT m FROM MediaEntity m WHERE m.isActive = true ORDER BY m.viewCount DESC LIMIT :limit")
    List<MediaEntity> findTopViewedMedia(@Param("limit") int limit);

    /**
     * Calculate total storage used in bytes.
     *
     * @return total file size in bytes
     */
    @Query("SELECT COALESCE(SUM(COALESCE(m.compressedFileSize, m.fileSize)), 0) FROM MediaEntity m WHERE m.isActive = true")
    Long calculateTotalStorageUsed();

    /**
     * Delete inactive media older than specified date.
     *
     * @param date the cutoff date
     * @return number of records deleted
     */
    @Query("DELETE FROM MediaEntity m WHERE m.isActive = false AND m.updatedAt < :date")
    int deleteInactiveMediaOlderThan(@Param("date") LocalDateTime date);
}
