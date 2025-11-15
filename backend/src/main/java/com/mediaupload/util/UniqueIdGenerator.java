package com.mediaupload.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers for media files.
 */
@Component
public class UniqueIdGenerator {

    /**
     * Generate a unique ID using UUID.
     *
     * @return a unique identifier string
     */
    public String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a short unique ID (8 characters).
     * Useful for URLs when full UUID is too long.
     *
     * @return a short unique identifier
     */
    public String generateShort() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
