-- Media Upload Application - Initial Database Schema
-- Version: 1.0.0
-- Description: Creates the media_files table with indexes for optimal performance

-- Create media_files table
CREATE TABLE IF NOT EXISTS media_files (
    id BIGSERIAL PRIMARY KEY,
    unique_id VARCHAR(36) NOT NULL UNIQUE,
    original_filename VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    media_type VARCHAR(20) NOT NULL,
    quality_level VARCHAR(20) NOT NULL,
    file_size BIGINT NOT NULL,
    compressed_file_size BIGINT,
    s3_bucket VARCHAR(255) NOT NULL,
    s3_key VARCHAR(500) NOT NULL,
    s3_url VARCHAR(1000) NOT NULL,
    qr_code_data TEXT,
    access_url VARCHAR(1000) NOT NULL,
    width INTEGER,
    height INTEGER,
    duration_seconds INTEGER,
    thumbnail_s3_key VARCHAR(500),
    view_count BIGINT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    expires_at TIMESTAMP,
    CONSTRAINT chk_media_type CHECK (media_type IN ('IMAGE', 'VIDEO')),
    CONSTRAINT chk_quality_level CHECK (quality_level IN ('HIGH', 'MEDIUM', 'LOW')),
    CONSTRAINT chk_file_size CHECK (file_size > 0),
    CONSTRAINT chk_view_count CHECK (view_count >= 0)
);

-- Create indexes for optimized queries
CREATE INDEX idx_unique_id ON media_files(unique_id);
CREATE INDEX idx_created_at ON media_files(created_at DESC);
CREATE INDEX idx_media_type ON media_files(media_type) WHERE is_active = true;
CREATE INDEX idx_expires_at ON media_files(expires_at) WHERE expires_at IS NOT NULL AND is_active = true;
CREATE INDEX idx_view_count ON media_files(view_count DESC) WHERE is_active = true;
CREATE INDEX idx_is_active ON media_files(is_active);

-- Create composite indexes for common query patterns
CREATE INDEX idx_media_type_created ON media_files(media_type, created_at DESC) WHERE is_active = true;
CREATE INDEX idx_active_created ON media_files(is_active, created_at DESC);

-- Add comments for documentation
COMMENT ON TABLE media_files IS 'Stores metadata for uploaded media files (images and videos)';
COMMENT ON COLUMN media_files.unique_id IS 'Unique identifier for the media file (UUID)';
COMMENT ON COLUMN media_files.qr_code_data IS 'Base64 encoded QR code image data';
COMMENT ON COLUMN media_files.view_count IS 'Number of times the media has been viewed';
COMMENT ON COLUMN media_files.is_active IS 'Soft delete flag - false indicates deleted media';
COMMENT ON COLUMN media_files.expires_at IS 'Optional expiration timestamp for temporary media';
COMMENT ON COLUMN media_files.compressed_file_size IS 'Size of file after compression (null if not compressed)';

-- Create function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to auto-update updated_at
CREATE TRIGGER update_media_files_updated_at
    BEFORE UPDATE ON media_files
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Create view for active media statistics
CREATE OR REPLACE VIEW media_statistics AS
SELECT
    media_type,
    quality_level,
    COUNT(*) as total_count,
    SUM(COALESCE(compressed_file_size, file_size)) as total_size_bytes,
    AVG(COALESCE(compressed_file_size, file_size)) as avg_size_bytes,
    SUM(view_count) as total_views,
    AVG(view_count) as avg_views
FROM media_files
WHERE is_active = true
GROUP BY media_type, quality_level;

COMMENT ON VIEW media_statistics IS 'Aggregated statistics for active media files by type and quality';
