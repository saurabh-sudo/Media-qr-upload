package com.mediaupload.service;

import com.mediaupload.exception.DatabaseException;
import com.mediaupload.exception.MediaNotFoundException;
import com.mediaupload.model.dto.MediaRetrievalResponse;
import com.mediaupload.model.dto.MediaUploadRequest;
import com.mediaupload.model.dto.MediaUploadResponse;
import com.mediaupload.model.entity.MediaEntity;
import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import com.mediaupload.repository.MediaRepository;
import com.mediaupload.util.FileTypeDetector;
import com.mediaupload.util.FileValidator;
import com.mediaupload.util.UniqueIdGenerator;
import com.mediaupload.util.UrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * Implementation of MediaService.
 * Orchestrates the complete media upload workflow.
 */
@Service
public class MediaServiceImpl implements MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);

    private final MediaRepository mediaRepository;
    private final S3Service s3Service;
    private final QRCodeService qrCodeService;
    private final CompressionService compressionService;
    private final FileValidator fileValidator;
    private final FileTypeDetector fileTypeDetector;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final UrlGenerator urlGenerator;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${app.media.default-expiration-days:365}")
    private int defaultExpirationDays;

    public MediaServiceImpl(MediaRepository mediaRepository,
                           S3Service s3Service,
                           QRCodeService qrCodeService,
                           CompressionService compressionService,
                           FileValidator fileValidator,
                           FileTypeDetector fileTypeDetector,
                           UniqueIdGenerator uniqueIdGenerator,
                           UrlGenerator urlGenerator) {
        this.mediaRepository = mediaRepository;
        this.s3Service = s3Service;
        this.qrCodeService = qrCodeService;
        this.compressionService = compressionService;
        this.fileValidator = fileValidator;
        this.fileTypeDetector = fileTypeDetector;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.urlGenerator = urlGenerator;
    }

    @Override
    @Transactional
    public MediaUploadResponse uploadMedia(MultipartFile file, MediaUploadRequest request) {
        logger.info("Starting media upload process for file: {}", file.getOriginalFilename());

        try {
            // Step 1: Validate the file
            fileValidator.validate(file);
            MediaType mediaType = fileTypeDetector.detectAndValidateMediaType(file);
            QualityLevel qualityLevel = request.getQualityLevel() != null ?
                    request.getQualityLevel() : QualityLevel.MEDIUM;

            logger.debug("File validated: type={}, quality={}", mediaType, qualityLevel);

            // Step 2: Generate unique identifier and URLs
            String uniqueId = uniqueIdGenerator.generate();
            String sanitizedFilename = fileValidator.sanitizeFilename(file.getOriginalFilename());
            String s3Key = urlGenerator.generateS3Key(uniqueId, sanitizedFilename);
            String accessUrl = urlGenerator.generateAccessUrl(uniqueId);

            logger.debug("Generated IDs: uniqueId={}, s3Key={}", uniqueId, s3Key);

            // Step 3: Compress media if supported
            byte[] fileData;
            Long compressedSize = null;
            if (compressionService.supportsCompression(mediaType)) {
                fileData = compressionService.compressMedia(file, qualityLevel, mediaType);
                compressedSize = (long) fileData.length;
            } else {
                fileData = file.getBytes();
            }

            // Step 4: Upload to S3
            String s3Url = s3Service.uploadFile(fileData, s3Key, file.getContentType());
            logger.info("File uploaded to S3: {}", s3Url);

            // Step 5: Generate QR Code
            String qrCodeData = qrCodeService.generateQRCode(accessUrl);
            logger.debug("QR code generated successfully");

            // Step 6: Calculate expiration date
            LocalDateTime expiresAt = null;
            if (request.getExpirationDays() != null && request.getExpirationDays() > 0) {
                expiresAt = LocalDateTime.now().plusDays(request.getExpirationDays());
            }

            // Step 7: Save to database
            MediaEntity entity = MediaEntity.builder()
                    .uniqueId(uniqueId)
                    .originalFilename(sanitizedFilename)
                    .contentType(file.getContentType())
                    .mediaType(mediaType)
                    .qualityLevel(qualityLevel)
                    .fileSize(file.getSize())
                    .compressedFileSize(compressedSize)
                    .s3Bucket(bucketName)
                    .s3Key(s3Key)
                    .s3Url(s3Url)
                    .qrCodeData(qrCodeData)
                    .accessUrl(accessUrl)
                    .expiresAt(expiresAt)
                    .isActive(true)
                    .viewCount(0L)
                    .build();

            MediaEntity savedEntity = mediaRepository.save(entity);
            logger.info("Media entity saved to database: id={}", savedEntity.getId());

            // Step 8: Return response
            return MediaUploadResponse.fromEntity(savedEntity, qrCodeData);

        } catch (Exception e) {
            logger.error("Media upload failed: {}", e.getMessage(), e);
            throw new DatabaseException("Failed to upload media", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MediaRetrievalResponse getMedia(String uniqueId) {
        logger.debug("Retrieving media: uniqueId={}", uniqueId);

        MediaEntity entity = mediaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new MediaNotFoundException(uniqueId));

        if (!entity.getIsActive()) {
            logger.warn("Attempt to access inactive media: uniqueId={}", uniqueId);
            throw new MediaNotFoundException(uniqueId);
        }

        if (entity.isExpired()) {
            logger.warn("Attempt to access expired media: uniqueId={}, expiresAt={}",
                    uniqueId, entity.getExpiresAt());
            throw new MediaNotFoundException(uniqueId);
        }

        logger.debug("Media retrieved successfully: uniqueId={}", uniqueId);
        return MediaRetrievalResponse.fromEntity(entity);
    }

    @Override
    @Transactional
    public void deleteMedia(String uniqueId) {
        logger.info("Deleting media: uniqueId={}", uniqueId);

        MediaEntity entity = mediaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new MediaNotFoundException(uniqueId));

        try {
            // Soft delete in database
            entity.setIsActive(false);
            mediaRepository.save(entity);

            // Delete from S3 (optional - could be done by a cleanup job)
            s3Service.deleteFile(entity.getS3Key());

            logger.info("Media deleted successfully: uniqueId={}", uniqueId);
        } catch (Exception e) {
            logger.error("Failed to delete media: uniqueId={}, error={}", uniqueId, e.getMessage(), e);
            throw new DatabaseException("Failed to delete media", e);
        }
    }

    @Override
    @Transactional
    public void incrementViewCount(String uniqueId) {
        logger.debug("Incrementing view count for media: uniqueId={}", uniqueId);

        MediaEntity entity = mediaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new MediaNotFoundException(uniqueId));

        entity.incrementViewCount();
        mediaRepository.save(entity);

        logger.debug("View count incremented: uniqueId={}, newCount={}", uniqueId, entity.getViewCount());
    }
}
