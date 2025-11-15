import React, { useState, useRef } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Stack,
  LinearProgress,
} from '@mui/material';
import {
  CloudUpload as UploadIcon,
  Image as ImageIcon,
  VideoLibrary as VideoIcon,
} from '@mui/icons-material';
import { QUALITY_LEVELS, QUALITY_DESCRIPTIONS } from '../utils/constants';
import { formatFileSize } from '../utils/fileUtils';
import ErrorMessage from './ErrorMessage';

const UploadForm = ({ onUpload, loading, error }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [qualityLevel, setQualityLevel] = useState(QUALITY_LEVELS.MEDIUM);
  const [expirationDays, setExpirationDays] = useState('');
  const [dragActive, setDragActive] = useState(false);
  const fileInputRef = useRef(null);

  const handleFileChange = (event) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedFile(file);
    }
  };

  const handleDrag = (event) => {
    event.preventDefault();
    event.stopPropagation();
    if (event.type === 'dragenter' || event.type === 'dragover') {
      setDragActive(true);
    } else if (event.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (event) => {
    event.preventDefault();
    event.stopPropagation();
    setDragActive(false);

    const file = event.dataTransfer.files?.[0];
    if (file) {
      setSelectedFile(file);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (selectedFile) {
      const expDays = expirationDays ? parseInt(expirationDays) : null;
      onUpload(selectedFile, qualityLevel, expDays);
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current?.click();
  };

  const getFileIcon = () => {
    if (!selectedFile) return <UploadIcon sx={{ fontSize: 60 }} />;
    if (selectedFile.type.startsWith('image/')) {
      return <ImageIcon sx={{ fontSize: 60 }} />;
    }
    if (selectedFile.type.startsWith('video/')) {
      return <VideoIcon sx={{ fontSize: 60 }} />;
    }
    return <UploadIcon sx={{ fontSize: 60 }} />;
  };

  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h5" gutterBottom align="center" color="primary">
          Upload Media File
        </Typography>

        <form onSubmit={handleSubmit}>
          <Stack spacing={3} mt={2}>
            {/* File upload area */}
            <Box
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
              sx={{
                border: '2px dashed',
                borderColor: dragActive ? 'primary.main' : 'grey.300',
                borderRadius: 2,
                p: 4,
                textAlign: 'center',
                cursor: 'pointer',
                backgroundColor: dragActive ? 'action.hover' : 'background.paper',
                transition: 'all 0.3s ease',
                '&:hover': {
                  borderColor: 'primary.main',
                  backgroundColor: 'action.hover',
                },
              }}
              onClick={handleButtonClick}
            >
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*,video/*"
                onChange={handleFileChange}
                style={{ display: 'none' }}
              />

              <Box color="primary.main" mb={2}>
                {getFileIcon()}
              </Box>

              {selectedFile ? (
                <>
                  <Typography variant="h6" gutterBottom>
                    {selectedFile.name}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    {formatFileSize(selectedFile.size)} • {selectedFile.type}
                  </Typography>
                </>
              ) : (
                <>
                  <Typography variant="h6" gutterBottom>
                    Drag and drop your file here
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    or click to browse
                  </Typography>
                  <Typography variant="caption" color="textSecondary" display="block" mt={1}>
                    Supports: Images (JPG, PNG, GIF, WebP, BMP) and Videos (MP4, MOV, AVI, WebM, MKV)
                  </Typography>
                </>
              )}
            </Box>

            {/* Quality selection */}
            <FormControl fullWidth>
              <InputLabel>Quality Level</InputLabel>
              <Select
                value={qualityLevel}
                label="Quality Level"
                onChange={(e) => setQualityLevel(e.target.value)}
              >
                {Object.values(QUALITY_LEVELS).map((level) => (
                  <MenuItem key={level} value={level}>
                    {level} - {QUALITY_DESCRIPTIONS[level]}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {/* Expiration days */}
            <TextField
              fullWidth
              label="Expiration Days (Optional)"
              type="number"
              value={expirationDays}
              onChange={(e) => setExpirationDays(e.target.value)}
              helperText="Leave empty for no expiration"
              inputProps={{ min: 1, max: 730 }}
            />

            {/* Error message */}
            {error && <ErrorMessage message={error} />}

            {/* Loading progress */}
            {loading && (
              <Box>
                <LinearProgress />
                <Typography variant="caption" color="textSecondary" align="center" mt={1}>
                  Uploading and processing...
                </Typography>
              </Box>
            )}

            {/* Submit button */}
            <Button
              type="submit"
              variant="contained"
              size="large"
              startIcon={<UploadIcon />}
              disabled={!selectedFile || loading}
              fullWidth
            >
              {loading ? 'Uploading...' : 'Upload'}
            </Button>
          </Stack>
        </form>
      </CardContent>
    </Card>
  );
};

export default UploadForm;
