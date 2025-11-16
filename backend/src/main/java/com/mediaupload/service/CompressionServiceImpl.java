package com.mediaupload.service;

import com.mediaupload.exception.CompressionException;
import com.mediaupload.model.enums.MediaType;
import com.mediaupload.model.enums.QualityLevel;
import com.mediaupload.strategy.CompressionStrategy;
import com.mediaupload.strategy.CompressionStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Implementation of CompressionService.
 * Handles media compression using appropriate strategies.
 */
@Service
public class CompressionServiceImpl implements CompressionService {

    private static final Logger logger = LoggerFactory.getLogger(CompressionServiceImpl.class);

    private final CompressionStrategyFactory strategyFactory;

    public CompressionServiceImpl(CompressionStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public byte[] compressMedia(MultipartFile file, QualityLevel qualityLevel, MediaType mediaType) {
        logger.info("Compressing media: filename={}, type={}, quality={}",
                file.getOriginalFilename(), mediaType, qualityLevel);

        // Videos are currently not compressed in this implementation
        // They are uploaded as-is to maintain compatibility and reduce processing time
        if (mediaType == MediaType.VIDEO) {
            logger.info("Video files are not compressed, returning original file");
            try {
                return file.getBytes();
            } catch (IOException e) {
                logger.error("Failed to read video file bytes: {}", e.getMessage(), e);
                throw new CompressionException("Failed to read video file", e);
            }
        }

        // Compress images using the appropriate strategy
        try {
            CompressionStrategy strategy = strategyFactory.getStrategy(qualityLevel, file.getContentType());
            byte[] compressedData = strategy.compress(file);

            long originalSize = file.getSize();
            long compressedSize = compressedData.length;
            double compressionRatio = ((originalSize - compressedSize) / (double) originalSize) * 100.0;

            logger.info("Compression completed: original={}KB, compressed={}KB, ratio={:.2f}%",
                    originalSize / 1024, compressedSize / 1024, compressionRatio);

            return compressedData;
        } catch (IOException e) {
            logger.error("Compression failed: {}", e.getMessage(), e);
            throw new CompressionException("Failed to compress media file", e);
        }
    }

    @Override
    public boolean supportsCompression(MediaType mediaType) {
        // Currently only images are compressed
        return mediaType == MediaType.IMAGE;
    }
}
