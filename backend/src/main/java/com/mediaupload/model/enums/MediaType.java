package com.mediaupload.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enum representing supported media types.
 * Includes validation for file extensions and MIME types.
 */
public enum MediaType {
    IMAGE(Set.of("image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"),
          Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp"),
          50 * 1024 * 1024), // 50MB max for images

    VIDEO(Set.of("video/mp4", "video/quicktime", "video/x-msvideo", "video/webm", "video/x-matroska"),
          Set.of("mp4", "mov", "avi", "webm", "mkv"),
          500 * 1024 * 1024); // 500MB max for videos (Free tier compliant)

    private final Set<String> mimeTypes;
    private final Set<String> extensions;
    private final long maxFileSize;

    MediaType(Set<String> mimeTypes, Set<String> extensions, long maxFileSize) {
        this.mimeTypes = mimeTypes;
        this.extensions = extensions;
        this.maxFileSize = maxFileSize;
    }

    public Set<String> getMimeTypes() {
        return mimeTypes;
    }

    public Set<String> getExtensions() {
        return extensions;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Determine media type from MIME type.
     *
     * @param mimeType the MIME type to check
     * @return the corresponding MediaType, or null if not supported
     */
    public static MediaType fromMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(type -> type.getMimeTypes().stream()
                        .anyMatch(mime -> mimeType.toLowerCase().startsWith(mime)))
                .findFirst()
                .orElse(null);
    }

    /**
     * Determine media type from file extension.
     *
     * @param extension the file extension to check (without dot)
     * @return the corresponding MediaType, or null if not supported
     */
    public static MediaType fromExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String ext = extension.toLowerCase().replaceAll("^\\.", "");
        return Arrays.stream(values())
                .filter(type -> type.getExtensions().contains(ext))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all supported MIME types across all media types.
     *
     * @return set of all supported MIME types
     */
    public static Set<String> getAllSupportedMimeTypes() {
        return Arrays.stream(values())
                .flatMap(type -> type.getMimeTypes().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Get all supported extensions across all media types.
     *
     * @return set of all supported extensions
     */
    public static Set<String> getAllSupportedExtensions() {
        return Arrays.stream(values())
                .flatMap(type -> type.getExtensions().stream())
                .collect(Collectors.toSet());
    }
}
