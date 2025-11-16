import React from 'react';
import {
  Box,
  Card,
  CardContent,
  CardMedia,
  Typography,
  Chip,
  Stack,
  Divider,
} from '@mui/material';
import {
  Image as ImageIcon,
  VideoLibrary as VideoIcon,
  Visibility as ViewIcon,
} from '@mui/icons-material';

const MediaViewer = ({ media }) => {
  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };

  const isImage = media.mediaType === 'IMAGE';
  const isVideo = media.mediaType === 'VIDEO';

  return (
    <Card elevation={3}>
      <Box sx={{ position: 'relative' }}>
        {isImage && (
          <CardMedia
            component="img"
            image={media.s3Url}
            alt={media.originalFilename}
            sx={{ maxHeight: 600, objectFit: 'contain' }}
          />
        )}
        {isVideo && (
          <CardMedia
            component="video"
            src={media.s3Url}
            controls
            sx={{ maxHeight: 600, width: '100%' }}
          />
        )}
      </Box>

      <CardContent>
        <Stack spacing={2}>
          <Box>
            <Typography variant="h5" gutterBottom>
              {media.originalFilename}
            </Typography>
          </Box>

          <Divider />

          <Box display="flex" gap={1} flexWrap="wrap">
            <Chip
              icon={isImage ? <ImageIcon /> : <VideoIcon />}
              label={media.mediaType}
              color="primary"
              size="small"
            />
            <Chip label={media.qualityLevel} color="secondary" size="small" />
            <Chip label={formatFileSize(media.fileSize)} size="small" />
            <Chip
              icon={<ViewIcon />}
              label={`${media.viewCount || 0} views`}
              size="small"
            />
          </Box>

          {(media.width || media.height) && (
            <Box>
              <Typography variant="subtitle2" color="textSecondary">
                Dimensions
              </Typography>
              <Typography variant="body2">
                {media.width} x {media.height}
              </Typography>
            </Box>
          )}

          {media.durationSeconds && (
            <Box>
              <Typography variant="subtitle2" color="textSecondary">
                Duration
              </Typography>
              <Typography variant="body2">
                {Math.floor(media.durationSeconds / 60)}:
                {(media.durationSeconds % 60).toString().padStart(2, '0')}
              </Typography>
            </Box>
          )}

          <Box>
            <Typography variant="subtitle2" color="textSecondary">
              Uploaded
            </Typography>
            <Typography variant="body2">
              {formatDate(media.createdAt)}
            </Typography>
          </Box>

          {media.expiresAt && (
            <Box>
              <Typography variant="subtitle2" color="textSecondary">
                Expires
              </Typography>
              <Typography variant="body2" color={media.isExpired ? 'error' : 'inherit'}>
                {formatDate(media.expiresAt)}
                {media.isExpired && ' (Expired)'}
              </Typography>
            </Box>
          )}
        </Stack>
      </CardContent>
    </Card>
  );
};

export default MediaViewer;
