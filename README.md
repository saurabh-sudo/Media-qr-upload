# Media QR Upload

A production-ready, full-stack web application that allows users to upload images and videos with quality selection, stores them in AWS S3, generates QR codes for mobile access, and serves content through a responsive interface.

## Features

- **Media Upload**: Support for images (JPEG, PNG, GIF, WebP, BMP) and videos (MP4, MOV, AVI, WebM, MKV)
- **Quality Selection**: Choose from High, Medium, or Low quality compression
- **QR Code Generation**: Automatic QR code generation for easy mobile access
- **AWS S3 Storage**: Secure cloud storage with AWS Free Tier compliance
- **Responsive UI**: Modern Material-UI interface
- **RESTful API**: Well-documented REST API
- **SOLID Principles**: Clean, maintainable code architecture
- **Comprehensive Logging**: Full application logging and error handling
- **Docker Support**: Containerized deployment
- **Database Persistence**: PostgreSQL with Flyway migrations

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- PostgreSQL 15
- AWS S3 SDK
- Maven
- ZXing (QR Code generation)
- Thumbnailator (Image compression)

### Frontend
- React 18
- Vite
- Material-UI (MUI)
- React Router v6
- Axios

### Infrastructure
- Docker & Docker Compose
- AWS EC2 (t2.micro)
- AWS RDS PostgreSQL (db.t3.micro)
- AWS S3
- Nginx

## Project Structure

```
media-qr-upload/
├── backend/               # Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/              # React application
│   ├── src/
│   ├── package.json
│   └── Dockerfile
├── deployment/            # Deployment configurations
│   ├── docker-compose.yml
│   ├── aws/              # AWS deployment scripts
│   └── scripts/          # Utility scripts
└── README.md
```

## Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL 15 (or use Docker)
- AWS Account (for S3)

### Environment Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd media-qr-upload
```

2. Configure AWS credentials:
```bash
# Create .env file in deployment directory
cp deployment/.env.example deployment/.env
# Edit .env with your AWS credentials
```

3. Start with Docker Compose:
```bash
cd deployment
docker-compose up -d
```

The application will be available at:
- Frontend: http://localhost:80
- Backend API: http://localhost:8080
- API Documentation: http://localhost:8080/api/health

### Local Development

#### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.yml`:

```yaml
aws:
  s3:
    bucket-name: your-bucket-name
    region: us-east-1
    access-key: ${AWS_ACCESS_KEY}
    secret-key: ${AWS_SECRET_KEY}

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mediaupload
    username: postgres
    password: your-password
```

### Frontend Configuration

Create `.env` file in frontend directory:

```env
VITE_API_URL=http://localhost:8080/api
```

## API Endpoints

### Media Upload
```
POST /api/media/upload
Content-Type: multipart/form-data

Parameters:
- file: MultipartFile (required)
- qualityLevel: String (HIGH, MEDIUM, LOW) (default: MEDIUM)
- expirationDays: Integer (optional)
```

### Get Media
```
GET /api/media/{uniqueId}
```

### Delete Media
```
DELETE /api/media/{uniqueId}
```

### Health Check
```
GET /api/health
GET /api/health/detailed
```

## Deployment

### Docker Deployment

```bash
cd deployment
docker-compose up -d
```

### AWS Deployment

1. Configure AWS CLI:
```bash
aws configure
```

2. Deploy using CloudFormation:
```bash
cd deployment/aws
./deploy.sh
```

3. Setup EC2 instance:
```bash
ssh -i your-key.pem ec2-user@your-ec2-ip
./setup-ec2.sh
```

## Maintenance Scripts

### Database Backup
```bash
./deployment/scripts/backup-db.sh
```

### S3 Cleanup
```bash
./deployment/scripts/cleanup-s3.sh
```

### Health Check
```bash
./deployment/scripts/health-check.sh
```

## Architecture

### Design Patterns Used

1. **Strategy Pattern**: Compression strategies for different quality levels
2. **Factory Pattern**: Compression strategy factory
3. **Repository Pattern**: Data access layer
4. **DTO Pattern**: Data transfer objects for API
5. **Service Layer Pattern**: Business logic separation

### Security Features

- File validation and sanitization
- MIME type detection
- Path traversal protection
- CORS configuration
- SQL injection protection (JPA)
- File size limits
- Content-Type validation

## Free Tier Compliance

This application is designed to run within AWS Free Tier limits:

- EC2: t2.micro instance (750 hours/month)
- RDS: db.t3.micro (750 hours/month)
- S3: 5GB storage, 20,000 GET requests, 2,000 PUT requests
- Data Transfer: 15GB/month

## Monitoring and Logging

- Application logs: `logs/application.log`
- Error logs: `logs/error.log`
- Health endpoints for monitoring
- Database query logging (configurable)

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.

## Authors

- Media Upload Team

## Acknowledgments

- Spring Boot framework
- React ecosystem
- AWS services
- Material-UI components
- ZXing library for QR codes
