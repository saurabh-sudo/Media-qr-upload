import { useState, useEffect } from 'react';
import { getMedia } from '../services/api';
import { handleApiError } from '../utils/errorHandler';

/**
 * Custom hook for viewing media
 */
export const useMediaView = (uniqueId) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [media, setMedia] = useState(null);

  useEffect(() => {
    const fetchMedia = async () => {
      if (!uniqueId) {
        setLoading(false);
        return;
      }

      setLoading(true);
      setError(null);

      try {
        const result = await getMedia(uniqueId);
        setMedia(result);
      } catch (err) {
        const errorMessage = handleApiError(err, {
          404: 'Media not found or has expired',
        });
        setError(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    fetchMedia();
  }, [uniqueId]);

  const refresh = async () => {
    if (!uniqueId) return;

    setLoading(true);
    setError(null);

    try {
      const result = await getMedia(uniqueId);
      setMedia(result);
    } catch (err) {
      const errorMessage = handleApiError(err);
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return {
    media,
    loading,
    error,
    refresh,
  };
};

export default useMediaView;
