import { useState } from 'react';
import { uploadMedia } from '../services/api';
import { validateFile } from '../utils/fileUtils';
import { handleApiError } from '../utils/errorHandler';

/**
 * Custom hook for media upload functionality
 */
export const useMediaUpload = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [uploadResult, setUploadResult] = useState(null);
  const [progress, setProgress] = useState(0);

  const upload = async (file, qualityLevel = 'MEDIUM', expirationDays = null) => {
    setLoading(true);
    setError(null);
    setUploadResult(null);
    setProgress(0);

    // Validate file
    const validationErrors = validateFile(file);
    if (validationErrors.length > 0) {
      setError(validationErrors.join(', '));
      setLoading(false);
      return null;
    }

    try {
      const result = await uploadMedia(file, qualityLevel, expirationDays);
      setUploadResult(result);
      setProgress(100);
      return result;
    } catch (err) {
      const errorMessage = handleApiError(err);
      setError(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const reset = () => {
    setLoading(false);
    setError(null);
    setUploadResult(null);
    setProgress(0);
  };

  return {
    upload,
    loading,
    error,
    uploadResult,
    progress,
    reset,
  };
};

export default useMediaUpload;
