package com.mediaupload.strategy;

import com.mediaupload.model.enums.QualityLevel;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Medium quality compression strategy.
 * Balanced compression for optimal size/quality ratio.
 */
@Component
public class MediumQualityStrategy implements CompressionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MediumQualityStrategy.class);
    private final QualityLevel qualityLevel = QualityLevel.MEDIUM;

    @Override
    public byte[] compress(MultipartFile file) throws IOException {
        logger.debug("Compressing image with MEDIUM quality strategy");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(qualityLevel.getMaxDimension(), qualityLevel.getMaxDimension())
                    .outputQuality(qualityLevel.getCompressionQuality())
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            logger.debug("Medium quality compression completed. Original: {} bytes, Compressed: {} bytes",
                    file.getSize(), compressed.length);
            return compressed;
        }
    }

    @Override
    public QualityLevel getQualityLevel() {
        return qualityLevel;
    }

    @Override
    public boolean supports(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }
}
