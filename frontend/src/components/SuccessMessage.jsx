import React from 'react';
import { Alert, AlertTitle, Box } from '@mui/material';

const SuccessMessage = ({ title = 'Success', message, onClose }) => {
  return (
    <Box my={2}>
      <Alert severity="success" onClose={onClose}>
        {title && <AlertTitle>{title}</AlertTitle>}
        {message}
      </Alert>
    </Box>
  );
};

export default SuccessMessage;
