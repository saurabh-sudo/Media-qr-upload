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
 * Low quality compression strategy.
 * Maximum compression for minimal file size.
 */
@Component
public class LowQualityStrategy implements CompressionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(LowQualityStrategy.class);
    private final QualityLevel qualityLevel = QualityLevel.LOW;

    @Override
    public byte[] compress(MultipartFile file) throws IOException {
        logger.debug("Compressing image with LOW quality strategy");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(qualityLevel.getMaxDimension(), qualityLevel.getMaxDimension())
                    .outputQuality(qualityLevel.getCompressionQuality())
                    .toOutputStream(outputStream);

            byte[] compressed = outputStream.toByteArray();
            logger.debug("Low quality compression completed. Original: {} bytes, Compressed: {} bytes",
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
