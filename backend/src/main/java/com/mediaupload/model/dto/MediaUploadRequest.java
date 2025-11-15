package com.mediaupload.model.dto;

import com.mediaupload.model.enums.QualityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for media upload requests.
 * Contains the file and quality level selection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaUploadRequest {

    @NotNull(message = "Quality level is required")
    private QualityLevel qualityLevel;

    private String description;

    private Integer expirationDays;
}
