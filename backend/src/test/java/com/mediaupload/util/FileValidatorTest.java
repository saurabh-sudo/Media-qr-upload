package com.mediaupload.util;

import com.mediaupload.exception.InvalidFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    private FileValidator fileValidator;
    private FileTypeDetector fileTypeDetector;

    @BeforeEach
    void setUp() {
        fileTypeDetector = new FileTypeDetector();
        fileValidator = new FileValidator(fileTypeDetector);
    }

    @Test
    void testSanitizeFilename() {
        String sanitized = fileValidator.sanitizeFilename("test file (1).jpg");
        assertNotNull(sanitized);
        assertFalse(sanitized.contains("("));
        assertFalse(sanitized.contains(")"));
    }

    @Test
    void testSanitizeFilenameWithPathTraversal() {
        String sanitized = fileValidator.sanitizeFilename("../../../etc/passwd");
        assertFalse(sanitized.contains(".."));
        assertFalse(sanitized.contains("/"));
    }

    @Test
    void testSanitizeNullFilename() {
        String sanitized = fileValidator.sanitizeFilename(null);
        assertEquals("unnamed", sanitized);
    }
}
