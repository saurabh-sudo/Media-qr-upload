import React from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Stack,
  Divider,
  Chip,
} from '@mui/material';
import {
  Download as DownloadIcon,
  Share as ShareIcon,
  ContentCopy as CopyIcon,
} from '@mui/icons-material';

const QRCodeDisplay = ({ uploadResult }) => {
  const handleDownloadQR = () => {
    const link = document.createElement('a');
    link.href = uploadResult.qrCodeData;
    link.download = `qr-code-${uploadResult.uniqueId}.png`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleCopyLink = async () => {
    try {
      await navigator.clipboard.writeText(uploadResult.accessUrl);
      alert('Link copied to clipboard!');
    } catch (err) {
      console.error('Failed to copy link:', err);
    }
  };

  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: 'Media QR Code',
          text: 'Check out this media!',
          url: uploadResult.accessUrl,
        });
      } catch (err) {
        console.error('Error sharing:', err);
      }
    } else {
      handleCopyLink();
    }
  };

  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
  };

  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h5" gutterBottom align="center" color="primary">
          Upload Successful!
        </Typography>

        <Divider sx={{ my: 2 }} />

        <Box display="flex" justifyContent="center" my={3}>
          <img
            src={uploadResult.qrCodeData}
            alt="QR Code"
            style={{ maxWidth: '300px', height: 'auto' }}
          />
        </Box>

        <Stack spacing={2}>
          <Box>
            <Typography variant="subtitle2" color="textSecondary">
              File Name
            </Typography>
            <Typography variant="body1">{uploadResult.originalFilename}</Typography>
          </Box>

          <Box>
            <Typography variant="subtitle2" color="textSecondary">
              Access URL
            </Typography>
            <Typography
              variant="body2"
              sx={{
                wordBreak: 'break-all',
                backgroundColor: '#f5f5f5',
                padding: 1,
                borderRadius: 1,
              }}
            >
              {uploadResult.accessUrl}
            </Typography>
          </Box>

          <Box display="flex" gap={1} flexWrap="wrap">
            <Chip label={uploadResult.mediaType} color="primary" size="small" />
            <Chip label={uploadResult.qualityLevel} color="secondary" size="small" />
            <Chip label={formatFileSize(uploadResult.fileSize)} size="small" />
            {uploadResult.compressionRatio > 0 && (
              <Chip
                label={`${uploadResult.compressionRatio.toFixed(1)}% compressed`}
                color="success"
                size="small"
              />
            )}
          </Box>

          <Stack direction="row" spacing={2} justifyContent="center" mt={2}>
            <Button
              variant="contained"
              startIcon={<DownloadIcon />}
              onClick={handleDownloadQR}
            >
              Download QR
            </Button>
            <Button
              variant="outlined"
              startIcon={<CopyIcon />}
              onClick={handleCopyLink}
            >
              Copy Link
            </Button>
            <Button
              variant="outlined"
              startIcon={<ShareIcon />}
              onClick={handleShare}
            >
              Share
            </Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  );
};

export default QRCodeDisplay;
