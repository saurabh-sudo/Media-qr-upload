import axios from 'axios';
import { API_BASE_URL } from '../utils/constants';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 120000, // 2 minutes for large file uploads
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    // Add any auth tokens here if needed
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle common errors
    if (error.response?.status === 401) {
      // Handle unauthorized
      console.error('Unauthorized access');
    } else if (error.response?.status === 500) {
      console.error('Server error');
    }
    return Promise.reject(error);
  }
);

/**
 * Upload media file
 */
export const uploadMedia = async (file, qualityLevel = 'MEDIUM', expirationDays = null) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('qualityLevel', qualityLevel);

  if (expirationDays) {
    formData.append('expirationDays', expirationDays);
  }

  const response = await apiClient.post('/media/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    onUploadProgress: (progressEvent) => {
      const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
      console.log(`Upload progress: ${percentCompleted}%`);
    },
  });

  return response.data;
};

/**
 * Get media by unique ID
 */
export const getMedia = async (uniqueId) => {
  const response = await apiClient.get(`/media/${uniqueId}`);
  return response.data;
};

/**
 * Delete media by unique ID
 */
export const deleteMedia = async (uniqueId) => {
  const response = await apiClient.delete(`/media/${uniqueId}`);
  return response.data;
};

/**
 * Check API health
 */
export const checkHealth = async () => {
  const response = await apiClient.get('/health');
  return response.data;
};

export default apiClient;
