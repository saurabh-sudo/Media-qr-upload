package com.mediaupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for the Media Upload Application.
 * This application provides QR code generation for uploaded media files with AWS S3 storage.
 *
 * @author Media Upload Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class MediaUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaUploadApplication.class, args);
    }
}
