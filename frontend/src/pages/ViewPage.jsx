import React from 'react';
import { Container, Typography, Box, Button } from '@mui/material';
import { Home as HomeIcon, CloudUpload as UploadIcon } from '@mui/icons-material';
import { useNavigate, useParams } from 'react-router-dom';
import MediaViewer from '../components/MediaViewer';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import useMediaView from '../hooks/useMediaView';

const ViewPage = () => {
  const navigate = useNavigate();
  const { uniqueId } = useParams();
  const { media, loading, error } = useMediaView(uniqueId);

  if (loading) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', py: 4 }}>
        <Container maxWidth="md">
          <LoadingSpinner message="Loading media..." />
        </Container>
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', py: 4 }}>
        <Container maxWidth="md">
          <ErrorMessage title="Error Loading Media" message={error} />
          <Box mt={4} display="flex" justifyContent="center" gap={2}>
            <Button
              variant="outlined"
              startIcon={<HomeIcon />}
              onClick={() => navigate('/')}
            >
              Home
            </Button>
            <Button
              variant="contained"
              startIcon={<UploadIcon />}
              onClick={() => navigate('/upload')}
            >
              Upload
            </Button>
          </Box>
        </Container>
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', py: 4 }}>
      <Container maxWidth="md">
        {/* Header */}
        <Box mb={4} textAlign="center">
          <Typography variant="h3" component="h1" gutterBottom fontWeight="bold">
            View Media
          </Typography>
        </Box>

        {/* Media Viewer */}
        {media && <MediaViewer media={media} />}

        {/* Actions */}
        <Box mt={4} display="flex" justifyContent="center" gap={2}>
          <Button
            variant="outlined"
            startIcon={<HomeIcon />}
            onClick={() => navigate('/')}
          >
            Home
          </Button>
          <Button
            variant="contained"
            startIcon={<UploadIcon />}
            onClick={() => navigate('/upload')}
          >
            Upload New
          </Button>
        </Box>
      </Container>
    </Box>
  );
};

export default ViewPage;
