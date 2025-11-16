// API Configuration
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Quality Levels
export const QUALITY_LEVELS = {
  HIGH: 'HIGH',
  MEDIUM: 'MEDIUM',
  LOW: 'LOW'
};

export const QUALITY_DESCRIPTIONS = {
  HIGH: 'High Quality - Minimal compression (1920px max)',
  MEDIUM: 'Medium Quality - Balanced compression (1280px max)',
  LOW: 'Low Quality - Maximum compression (854px max)'
};

// File Size Limits (in bytes)
export const MAX_FILE_SIZE = {
  IMAGE: 50 * 1024 * 1024, // 50MB
  VIDEO: 500 * 1024 * 1024 // 500MB
};

// Supported File Types
export const SUPPORTED_IMAGE_TYPES = [
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp',
  'image/bmp'
];

export const SUPPORTED_VIDEO_TYPES = [
  'video/mp4',
  'video/quicktime',
  'video/x-msvideo',
  'video/webm',
  'video/x-matroska'
];

export const ALL_SUPPORTED_TYPES = [
  ...SUPPORTED_IMAGE_TYPES,
  ...SUPPORTED_VIDEO_TYPES
];

// Messages
export const MESSAGES = {
  UPLOAD_SUCCESS: 'File uploaded successfully!',
  UPLOAD_ERROR: 'Failed to upload file. Please try again.',
  FILE_TOO_LARGE: 'File size exceeds the maximum allowed size.',
  INVALID_FILE_TYPE: 'File type not supported.',
  NETWORK_ERROR: 'Network error. Please check your connection.',
};
