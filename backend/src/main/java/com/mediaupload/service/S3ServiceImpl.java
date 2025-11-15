package com.mediaupload.service;

import com.mediaupload.exception.S3UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

/**
 * Implementation of S3Service for AWS S3 operations.
 */
@Service
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String uploadFile(byte[] fileData, String key, String contentType) {
        try {
            logger.info("Uploading file to S3: bucket={}, key={}", bucketName, key);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

            String s3Url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
            logger.info("File uploaded successfully to S3: {}", s3Url);

            return s3Url;
        } catch (S3Exception e) {
            logger.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String key) {
        try {
            return uploadFile(file.getBytes(), key, file.getContentType());
        } catch (IOException e) {
            logger.error("Failed to read file bytes: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to read file data", e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            logger.info("Deleting file from S3: bucket={}, key={}", bucketName, key);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            logger.info("File deleted successfully from S3: {}", key);
        } catch (S3Exception e) {
            logger.error("Failed to delete file from S3: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to delete file from S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String generatePresignedUrl(String key, int durationMinutes) {
        try {
            logger.debug("Generating pre-signed URL for key: {}, duration: {} minutes", key, durationMinutes);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(durationMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            logger.debug("Generated pre-signed URL: {}", url);
            return url;
        } catch (S3Exception e) {
            logger.error("Failed to generate pre-signed URL: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to generate pre-signed URL: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean fileExists(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            logger.error("Error checking file existence: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to check file existence: " + e.getMessage(), e);
        }
    }

    @Override
    public long getFileSize(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            HeadObjectResponse response = s3Client.headObject(headObjectRequest);
            return response.contentLength();
        } catch (S3Exception e) {
            logger.error("Failed to get file size: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to get file size: " + e.getMessage(), e);
        }
    }
}
