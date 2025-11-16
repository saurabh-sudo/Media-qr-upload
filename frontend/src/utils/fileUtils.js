import {
  MAX_FILE_SIZE,
  SUPPORTED_IMAGE_TYPES,
  SUPPORTED_VIDEO_TYPES,
  ALL_SUPPORTED_TYPES
} from './constants';

/**
 * Validate file type
 */
export const isValidFileType = (file) => {
  return ALL_SUPPORTED_TYPES.includes(file.type);
};

/**
 * Validate file size
 */
export const isValidFileSize = (file) => {
  const isImage = SUPPORTED_IMAGE_TYPES.includes(file.type);
  const isVideo = SUPPORTED_VIDEO_TYPES.includes(file.type);

  if (isImage) {
    return file.size <= MAX_FILE_SIZE.IMAGE;
  }
  if (isVideo) {
    return file.size <= MAX_FILE_SIZE.VIDEO;
  }
  return false;
};

/**
 * Format file size for display
 */
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes';

  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
};

/**
 * Get file type category (image or video)
 */
export const getFileCategory = (file) => {
  if (SUPPORTED_IMAGE_TYPES.includes(file.type)) {
    return 'IMAGE';
  }
  if (SUPPORTED_VIDEO_TYPES.includes(file.type)) {
    return 'VIDEO';
  }
  return 'UNKNOWN';
};

/**
 * Get max file size for file category
 */
export const getMaxFileSize = (file) => {
  const category = getFileCategory(file);
  return MAX_FILE_SIZE[category] || 0;
};

/**
 * Validate file
 */
export const validateFile = (file) => {
  const errors = [];

  if (!file) {
    errors.push('No file selected');
    return errors;
  }

  if (!isValidFileType(file)) {
    errors.push('File type not supported');
  }

  if (!isValidFileSize(file)) {
    const maxSize = formatFileSize(getMaxFileSize(file));
    errors.push(`File size exceeds maximum (${maxSize})`);
  }

  return errors;
};
