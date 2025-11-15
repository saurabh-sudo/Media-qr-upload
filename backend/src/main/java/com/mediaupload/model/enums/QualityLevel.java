package com.mediaupload.model.enums;

/**
 * Enum representing different quality levels for media compression.
 * Each quality level has associated compression parameters.
 */
public enum QualityLevel {
    HIGH(0.9f, 1920, "High quality - minimal compression"),
    MEDIUM(0.7f, 1280, "Medium quality - balanced compression"),
    LOW(0.5f, 854, "Low quality - maximum compression");

    private final float compressionQuality;
    private final int maxDimension;
    private final String description;

    QualityLevel(float compressionQuality, int maxDimension, String description) {
        this.compressionQuality = compressionQuality;
        this.maxDimension = maxDimension;
        this.description = description;
    }

    public float getCompressionQuality() {
        return compressionQuality;
    }

    public int getMaxDimension() {
        return maxDimension;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Parse quality level from string, with fallback to MEDIUM if invalid.
     *
     * @param value the string value to parse
     * @return the corresponding QualityLevel
     */
    public static QualityLevel fromString(String value) {
        if (value == null) {
            return MEDIUM;
        }
        try {
            return QualityLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEDIUM;
        }
    }
}
