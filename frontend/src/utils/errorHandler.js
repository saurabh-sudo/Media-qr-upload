import { MESSAGES } from './constants';

/**
 * Extract error message from error object
 */
export const getErrorMessage = (error) => {
  if (error.response) {
    // Server responded with error
    return error.response.data?.message || error.response.data?.error || MESSAGES.UPLOAD_ERROR;
  } else if (error.request) {
    // Request made but no response
    return MESSAGES.NETWORK_ERROR;
  } else {
    // Something else happened
    return error.message || MESSAGES.UPLOAD_ERROR;
  }
};

/**
 * Handle API errors
 */
export const handleApiError = (error, customMessages = {}) => {
  const message = getErrorMessage(error);
  console.error('API Error:', message, error);
  return customMessages[error.response?.status] || message;
};

/**
 * Log error to console in development
 */
export const logError = (error, context = '') => {
  if (import.meta.env.DEV) {
    console.error(`[${context}] Error:`, error);
  }
};
