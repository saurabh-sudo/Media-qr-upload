# Media QR Upload - Frontend

React frontend application for the Media QR Upload service.

## Technology Stack

- **React**: 18.2.0
- **Build Tool**: Vite 5.0
- **UI Framework**: Material-UI (MUI) 5.14
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **State Management**: React Hooks

## Project Structure

```
frontend/
├── src/
│   ├── components/          # Reusable components
│   │   ├── UploadForm.jsx
│   │   ├── QRCodeDisplay.jsx
│   │   ├── MediaViewer.jsx
│   │   ├── LoadingSpinner.jsx
│   │   ├── ErrorMessage.jsx
│   │   └── SuccessMessage.jsx
│   ├── pages/               # Page components
│   │   ├── HomePage.jsx
│   │   ├── UploadPage.jsx
│   │   └── ViewPage.jsx
│   ├── services/            # API services
│   │   └── api.js
│   ├── hooks/               # Custom React hooks
│   │   ├── useMediaUpload.js
│   │   └── useMediaView.js
│   ├── utils/               # Utility functions
│   │   ├── constants.js
│   │   ├── fileUtils.js
│   │   └── errorHandler.js
│   ├── styles/              # Style files
│   │   ├── App.css
│   │   └── theme.js
│   ├── App.jsx              # Main App component
│   └── main.jsx             # Entry point
├── public/                  # Static assets
├── index.html               # HTML template
├── package.json             # Dependencies
├── vite.config.js           # Vite configuration
└── Dockerfile               # Docker configuration
```

## Setup

### Prerequisites

- Node.js 18+ and npm

### Installation

1. Install dependencies:
```bash
npm install
```

2. Configure environment:

Create `.env` file in the root directory:
```env
VITE_API_URL=http://localhost:8080/api
```

3. Run development server:
```bash
npm run dev
```

The application will start on `http://localhost:3000`

## Available Scripts

### Development
```bash
npm run dev
```
Starts the development server with hot reload.

### Build
```bash
npm run build
```
Builds the application for production.

### Preview
```bash
npm run preview
```
Preview the production build locally.

### Lint
```bash
npm run lint
```
Run ESLint to check code quality.

## Components

### UploadForm
File upload form with drag-and-drop support.

**Props:**
- `onUpload`: Function called when file is uploaded
- `loading`: Boolean indicating upload in progress
- `error`: Error message to display

**Features:**
- Drag and drop file upload
- Quality level selection
- Optional expiration days
- File validation
- Progress indicator

### QRCodeDisplay
Displays QR code and media information after upload.

**Props:**
- `uploadResult`: Upload result object containing QR code and media details

**Features:**
- QR code display
- Download QR code
- Copy access URL
- Share functionality
- File statistics

### MediaViewer
Displays media content (image or video).

**Props:**
- `media`: Media object containing URL and metadata

**Features:**
- Image/video display
- Metadata display
- View count
- Expiration information
- Responsive design

## Custom Hooks

### useMediaUpload
Hook for handling media upload functionality.

```javascript
const { upload, loading, error, uploadResult, reset } = useMediaUpload();
```

**Returns:**
- `upload(file, qualityLevel, expirationDays)`: Upload function
- `loading`: Upload in progress
- `error`: Error message
- `uploadResult`: Upload result
- `reset()`: Reset state

### useMediaView
Hook for viewing media.

```javascript
const { media, loading, error, refresh } = useMediaView(uniqueId);
```

**Returns:**
- `media`: Media object
- `loading`: Loading state
- `error`: Error message
- `refresh()`: Refresh media data

## API Integration

### API Service
Located in `src/services/api.js`

**Methods:**
- `uploadMedia(file, qualityLevel, expirationDays)`: Upload media
- `getMedia(uniqueId)`: Get media by ID
- `deleteMedia(uniqueId)`: Delete media
- `checkHealth()`: Check API health

**Configuration:**
- Base URL from environment variable
- 2-minute timeout for uploads
- Automatic retry on failure
- Request/response interceptors

## Routing

### Routes

- `/`: Home page with features and how it works
- `/upload`: Upload page
- `/view/:uniqueId`: View media page

### Navigation

Navigation between pages using React Router:
```javascript
import { useNavigate } from 'react-router-dom';

const navigate = useNavigate();
navigate('/upload');
```

## Theming

Custom Material-UI theme in `src/styles/theme.js`

**Customizations:**
- Primary color: Blue (#1976d2)
- Secondary color: Purple (#9c27b0)
- Custom typography
- Component overrides
- Responsive breakpoints

## File Validation

### Supported File Types

**Images:**
- JPEG (.jpg, .jpeg)
- PNG (.png)
- GIF (.gif)
- WebP (.webp)
- BMP (.bmp)

**Videos:**
- MP4 (.mp4)
- MOV (.mov)
- AVI (.avi)
- WebM (.webm)
- MKV (.mkv)

### Size Limits

- Images: 50 MB max
- Videos: 500 MB max

## Quality Levels

- **HIGH**: Minimal compression, 1920px max dimension, 90% quality
- **MEDIUM**: Balanced compression, 1280px max dimension, 70% quality
- **LOW**: Maximum compression, 854px max dimension, 50% quality

## Environment Variables

- `VITE_API_URL`: Backend API base URL

## Building for Production

### Build
```bash
npm run build
```

Outputs to `dist/` directory.

### Docker Build
```bash
docker build -t media-qr-frontend .
```

### Deploy
The built files in `dist/` can be served by any static file server:
- Nginx
- Apache
- AWS S3 + CloudFront
- Netlify
- Vercel

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Accessibility

- Semantic HTML
- ARIA labels
- Keyboard navigation
- Screen reader support
- Color contrast compliance

## Performance Optimization

- Code splitting
- Lazy loading
- Image optimization
- Gzip compression
- Cache headers
- Minification

## Troubleshooting

### CORS Issues
Ensure backend CORS configuration includes frontend URL.

### API Connection
Check `VITE_API_URL` environment variable.

### Build Errors
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Port Already in Use
Change port in `vite.config.js`:
```javascript
server: {
  port: 3001
}
```

## Contributing

1. Follow React best practices
2. Use functional components and hooks
3. Add PropTypes for components
4. Write meaningful component names
5. Keep components small and focused
6. Use Material-UI components
7. Follow the project structure

## License

MIT License
