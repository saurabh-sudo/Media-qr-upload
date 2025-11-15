import React from 'react';
import { Container, Typography, Box, Button, Grid, Card, CardContent } from '@mui/material';
import {
  CloudUpload as UploadIcon,
  QrCode as QrCodeIcon,
  Security as SecurityIcon,
  Speed as SpeedIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const navigate = useNavigate();

  const features = [
    {
      icon: <UploadIcon sx={{ fontSize: 60 }} />,
      title: 'Easy Upload',
      description: 'Upload images and videos with support for multiple formats',
    },
    {
      icon: <QrCodeIcon sx={{ fontSize: 60 }} />,
      title: 'QR Code Generation',
      description: 'Automatically generate QR codes for easy mobile access',
    },
    {
      icon: <SpeedIcon sx={{ fontSize: 60 }} />,
      title: 'Quality Selection',
      description: 'Choose from high, medium, or low quality compression',
    },
    {
      icon: <SecurityIcon sx={{ fontSize: 60 }} />,
      title: 'Secure Storage',
      description: 'Files stored securely on AWS S3 with optional expiration',
    },
  ];

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      {/* Hero Section */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #1976d2 0%, #9c27b0 100%)',
          color: 'white',
          py: 10,
          textAlign: 'center',
        }}
      >
        <Container maxWidth="md">
          <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
            Media QR Upload
          </Typography>
          <Typography variant="h5" paragraph sx={{ mb: 4 }}>
            Upload images and videos, generate QR codes, share instantly
          </Typography>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/upload')}
            sx={{
              backgroundColor: 'white',
              color: 'primary.main',
              px: 4,
              py: 1.5,
              fontSize: '1.1rem',
              '&:hover': {
                backgroundColor: 'grey.100',
              },
            }}
            startIcon={<UploadIcon />}
          >
            Start Uploading
          </Button>
        </Container>
      </Box>

      {/* Features Section */}
      <Container maxWidth="lg" sx={{ py: 8 }}>
        <Typography variant="h3" align="center" gutterBottom fontWeight="bold" mb={6}>
          Features
        </Typography>

        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card
                elevation={2}
                sx={{
                  height: '100%',
                  textAlign: 'center',
                  transition: 'transform 0.3s ease',
                  '&:hover': {
                    transform: 'translateY(-8px)',
                    boxShadow: 6,
                  },
                }}
              >
                <CardContent>
                  <Box color="primary.main" mb={2}>
                    {feature.icon}
                  </Box>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* How It Works Section */}
      <Box sx={{ backgroundColor: 'grey.50', py: 8 }}>
        <Container maxWidth="md">
          <Typography variant="h3" align="center" gutterBottom fontWeight="bold" mb={6}>
            How It Works
          </Typography>

          <Grid container spacing={4}>
            <Grid item xs={12} md={4}>
              <Box textAlign="center">
                <Typography
                  variant="h2"
                  color="primary"
                  sx={{
                    fontWeight: 'bold',
                    opacity: 0.2,
                    mb: 2,
                  }}
                >
                  1
                </Typography>
                <Typography variant="h6" gutterBottom fontWeight="bold">
                  Upload Your File
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Select your image or video and choose quality level
                </Typography>
              </Box>
            </Grid>

            <Grid item xs={12} md={4}>
              <Box textAlign="center">
                <Typography
                  variant="h2"
                  color="primary"
                  sx={{
                    fontWeight: 'bold',
                    opacity: 0.2,
                    mb: 2,
                  }}
                >
                  2
                </Typography>
                <Typography variant="h6" gutterBottom fontWeight="bold">
                  Get Your QR Code
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Receive a unique QR code for your media
                </Typography>
              </Box>
            </Grid>

            <Grid item xs={12} md={4}>
              <Box textAlign="center">
                <Typography
                  variant="h2"
                  color="primary"
                  sx={{
                    fontWeight: 'bold',
                    opacity: 0.2,
                    mb: 2,
                  }}
                >
                  3
                </Typography>
                <Typography variant="h6" gutterBottom fontWeight="bold">
                  Share Anywhere
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Share the QR code or link to access your media
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Container>
      </Box>

      {/* CTA Section */}
      <Box sx={{ py: 8, textAlign: 'center' }}>
        <Container maxWidth="sm">
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Ready to get started?
          </Typography>
          <Typography variant="body1" paragraph color="textSecondary">
            Upload your first file and generate a QR code in seconds
          </Typography>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/upload')}
            startIcon={<UploadIcon />}
            sx={{ px: 4, py: 1.5 }}
          >
            Upload Now
          </Button>
        </Container>
      </Box>
    </Box>
  );
};

export default HomePage;
