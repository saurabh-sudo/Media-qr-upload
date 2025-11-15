package com.mediaupload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller.
 * Provides application health and status information.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Autowired
    private DataSource dataSource;

    /**
     * Basic health check endpoint.
     *
     * @return health status
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Media QR Upload Service");

        return ResponseEntity.ok(health);
    }

    /**
     * Detailed health check with component status.
     *
     * @return detailed health information
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Media QR Upload Service");

        // Application info
        Map<String, String> app = new HashMap<>();
        if (buildProperties != null) {
            app.put("name", buildProperties.getName());
            app.put("version", buildProperties.getVersion());
            app.put("time", buildProperties.getTime().toString());
        } else {
            app.put("name", "media-qr-upload");
            app.put("version", "1.0.0");
        }
        health.put("application", app);

        // Database health
        Map<String, String> database = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            database.put("status", connection.isValid(1) ? "UP" : "DOWN");
            database.put("database", connection.getCatalog());
        } catch (Exception e) {
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
        }
        health.put("database", database);

        return ResponseEntity.ok(health);
    }
}
