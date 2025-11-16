import React from 'react';
import { Container, Typography, Box, Button, Grid } from '@mui/material';
import { Home as HomeIcon, Refresh as RefreshIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import UploadForm from '../components/UploadForm';
import QRCodeDisplay from '../components/QRCodeDisplay';
import useMediaUpload from '../hooks/useMediaUpload';

const UploadPage = () => {
  const navigate = useNavigate();
  const { upload, loading, error, uploadResult, reset } = useMediaUpload();

  const handleUpload = async (file, qualityLevel, expirationDays) => {
    await upload(file, qualityLevel, expirationDays);
  };

  const handleReset = () => {
    reset();
  };

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', py: 4 }}>
      <Container maxWidth="md">
        {/* Header */}
        <Box mb={4} textAlign="center">
          <Typography variant="h3" component="h1" gutterBottom fontWeight="bold">
            Upload Media
          </Typography>
          <Typography variant="body1" color="textSecondary">
            Upload your images or videos and get instant QR codes
          </Typography>
        </Box>

        {/* Content */}
        <Grid container spacing={4}>
          <Grid item xs={12}>
            {!uploadResult ? (
              <UploadForm onUpload={handleUpload} loading={loading} error={error} />
            ) : (
              <QRCodeDisplay uploadResult={uploadResult} />
            )}
          </Grid>
        </Grid>

        {/* Actions */}
        <Box mt={4} display="flex" justifyContent="center" gap={2}>
          <Button
            variant="outlined"
            startIcon={<HomeIcon />}
            onClick={() => navigate('/')}
          >
            Home
          </Button>
          {uploadResult && (
            <Button
              variant="contained"
              startIcon={<RefreshIcon />}
              onClick={handleReset}
            >
              Upload Another
            </Button>
          )}
        </Box>
      </Container>
    </Box>
  );
};

export default UploadPage;
