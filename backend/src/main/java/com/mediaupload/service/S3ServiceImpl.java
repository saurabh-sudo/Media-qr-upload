package com.mediaupload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mediaupload.exception.S3UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Implementation of S3Service for AWS S3 operations using AWS SDK 1.x (Java 8 compatible).
 */
@Service
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFile(byte[] fileData, String key, String contentType) {
        try {
            logger.info("Uploading file to S3: bucket={}, key={}", bucketName, key);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileData.length);
            metadata.setContentType(contentType);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);

            amazonS3.putObject(putObjectRequest);

            String s3Url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
            logger.info("File uploaded successfully to S3: {}", s3Url);

            return s3Url;
        } catch (AmazonServiceException e) {
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

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
            amazonS3.deleteObject(deleteObjectRequest);

            logger.info("File deleted successfully from S3: {}", key);
        } catch (AmazonServiceException e) {
            logger.error("Failed to delete file from S3: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to delete file from S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String generatePresignedUrl(String key, int durationMinutes) {
        try {
            logger.debug("Generating pre-signed URL for key: {}, duration: {} minutes", key, durationMinutes);

            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += durationMinutes * 60 * 1000;
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);

            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            String urlString = url.toString();

            logger.debug("Generated pre-signed URL: {}", urlString);
            return urlString;
        } catch (AmazonServiceException e) {
            logger.error("Failed to generate pre-signed URL: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to generate pre-signed URL: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean fileExists(String key) {
        try {
            return amazonS3.doesObjectExist(bucketName, key);
        } catch (AmazonServiceException e) {
            logger.error("Error checking file existence: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to check file existence: " + e.getMessage(), e);
        }
    }

    @Override
    public long getFileSize(String key) {
        try {
            ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, key);
            return metadata.getContentLength();
        } catch (AmazonServiceException e) {
            logger.error("Failed to get file size: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to get file size: " + e.getMessage(), e);
        }
    }
}
