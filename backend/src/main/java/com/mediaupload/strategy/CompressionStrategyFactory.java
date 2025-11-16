package com.mediaupload.strategy;

import com.mediaupload.exception.CompressionException;
import com.mediaupload.model.enums.QualityLevel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for creating appropriate compression strategies.
 * Implements the Factory design pattern.
 */
@Component
public class CompressionStrategyFactory {

    private final Map<QualityLevel, CompressionStrategy> strategies;

    public CompressionStrategyFactory(List<CompressionStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        CompressionStrategy::getQualityLevel,
                        Function.identity()
                ));
    }

    /**
     * Get the appropriate compression strategy for the given quality level.
     *
     * @param qualityLevel the desired quality level
     * @return the compression strategy
     * @throws CompressionException if no strategy found for quality level
     */
    public CompressionStrategy getStrategy(QualityLevel qualityLevel) {
        CompressionStrategy strategy = strategies.get(qualityLevel);
        if (strategy == null) {
            throw new CompressionException(
                String.format("No compression strategy found for quality level: %s", qualityLevel)
            );
        }
        return strategy;
    }

    /**
     * Get strategy for quality level and verify content type support.
     *
     * @param qualityLevel the desired quality level
     * @param contentType the content type to validate
     * @return the compression strategy
     * @throws CompressionException if content type not supported
     */
    public CompressionStrategy getStrategy(QualityLevel qualityLevel, String contentType) {
        CompressionStrategy strategy = getStrategy(qualityLevel);
        if (!strategy.supports(contentType)) {
            throw new CompressionException(
                String.format("Quality level %s does not support content type: %s",
                        qualityLevel, contentType)
            );
        }
        return strategy;
    }
}
