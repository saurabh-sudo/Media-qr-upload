# Media QR Upload - Backend

Spring Boot backend service for the Media QR Upload application.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Database**: PostgreSQL 15
- **Build Tool**: Maven
- **Cloud Storage**: AWS S3
- **QR Code**: ZXing
- **Image Processing**: Thumbnailator
- **Database Migration**: Flyway

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/mediaupload/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── model/               # Domain models
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── dto/             # Data transfer objects
│   │   │   │   └── enums/           # Enumerations
│   │   │   ├── exception/           # Custom exceptions
│   │   │   ├── util/                # Utility classes
│   │   │   └── strategy/            # Compression strategies
│   │   └── resources/
│   │       ├── application.yml      # Main configuration
│   │       ├── application-dev.yml  # Dev configuration
│   │       ├── application-prod.yml # Prod configuration
│   │       └── db/migration/        # Flyway migrations
│   └── test/                        # Unit and integration tests
├── pom.xml                          # Maven configuration
└── Dockerfile                       # Docker configuration
```

## Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15
- AWS Account with S3 access

### Installation

1. Install dependencies:
```bash
mvn clean install
```

2. Configure database:
```bash
# Create PostgreSQL database
createdb mediaupload
```

3. Configure application properties:

Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mediaupload
    username: your_username
    password: your_password

aws:
  s3:
    bucket-name: your-bucket-name
    region: us-east-1
    access-key: your_access_key
    secret-key: your_secret_key
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Media Upload

**POST** `/api/media/upload`

Upload a media file with quality selection.

**Request:**
- Content-Type: `multipart/form-data`
- Parameters:
  - `file`: The file to upload (required)
  - `qualityLevel`: HIGH | MEDIUM | LOW (optional, default: MEDIUM)
  - `expirationDays`: Number of days until expiration (optional)

**Response:**
```json
{
  "uniqueId": "uuid",
  "originalFilename": "example.jpg",
  "mediaType": "IMAGE",
  "qualityLevel": "MEDIUM",
  "fileSize": 1024000,
  "compressedFileSize": 512000,
  "compressionRatio": 50.0,
  "qrCodeData": "data:image/png;base64,...",
  "accessUrl": "http://localhost:3000/view/uuid",
  "createdAt": "2024-01-01T12:00:00",
  "message": "Media uploaded successfully"
}
```

### Get Media

**GET** `/api/media/{uniqueId}`

Retrieve media information by unique ID.

**Response:**
```json
{
  "uniqueId": "uuid",
  "originalFilename": "example.jpg",
  "mediaType": "IMAGE",
  "qualityLevel": "MEDIUM",
  "contentType": "image/jpeg",
  "fileSize": 512000,
  "s3Url": "https://bucket.s3.amazonaws.com/...",
  "viewCount": 5,
  "createdAt": "2024-01-01T12:00:00",
  "isExpired": false
}
```

### Delete Media

**DELETE** `/api/media/{uniqueId}`

Delete media by unique ID (soft delete).

**Response:** 204 No Content

### Health Check

**GET** `/api/health`

Basic health check.

**GET** `/api/health/detailed`

Detailed health check with component status.

## Architecture

### Design Patterns

1. **Strategy Pattern**: Used for compression strategies (HighQualityStrategy, MediumQualityStrategy, LowQualityStrategy)
2. **Factory Pattern**: CompressionStrategyFactory for creating compression strategies
3. **Repository Pattern**: JPA repositories for data access
4. **Service Layer**: Business logic separation
5. **DTO Pattern**: Data transfer objects for API responses

### Key Components

#### Services

- **MediaService**: Main orchestration service
- **S3Service**: AWS S3 operations
- **QRCodeService**: QR code generation
- **CompressionService**: Media compression

#### Strategies

- **HighQualityStrategy**: 90% quality, 1920px max
- **MediumQualityStrategy**: 70% quality, 1280px max
- **LowQualityStrategy**: 50% quality, 854px max

#### Utilities

- **FileValidator**: File validation and sanitization
- **FileTypeDetector**: MIME type detection using Apache Tika
- **UniqueIdGenerator**: UUID generation
- **UrlGenerator**: URL generation for media access

## Database Schema

### media_files Table

```sql
- id: BIGSERIAL PRIMARY KEY
- unique_id: VARCHAR(36) UNIQUE
- original_filename: VARCHAR(500)
- content_type: VARCHAR(100)
- media_type: VARCHAR(20) (IMAGE, VIDEO)
- quality_level: VARCHAR(20) (HIGH, MEDIUM, LOW)
- file_size: BIGINT
- compressed_file_size: BIGINT
- s3_bucket: VARCHAR(255)
- s3_key: VARCHAR(500)
- s3_url: VARCHAR(1000)
- qr_code_data: TEXT
- access_url: VARCHAR(1000)
- view_count: BIGINT
- is_active: BOOLEAN
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
- expires_at: TIMESTAMP
```

## Configuration Profiles

### Development (dev)
- Detailed logging
- Show SQL queries
- Local database

### Production (prod)
- Minimal logging
- Optimized database pool
- Error details hidden

## Testing

Run tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=MediaServiceTest
```

## Building

### JAR Build
```bash
mvn clean package
```

### Docker Build
```bash
docker build -t media-qr-backend .
```

## Environment Variables

- `SPRING_PROFILE`: Active profile (dev, prod)
- `DATABASE_URL`: PostgreSQL connection URL
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `AWS_S3_BUCKET`: S3 bucket name
- `AWS_REGION`: AWS region
- `AWS_ACCESS_KEY`: AWS access key
- `AWS_SECRET_KEY`: AWS secret key

## Logging

Logs are written to:
- Console (all profiles)
- `logs/application.log` (rolling, 10MB max, 30 days)
- `logs/error.log` (errors only, 60 days)

## Security

- Path traversal protection
- File type validation
- File size limits
- MIME type detection
- CORS configuration
- SQL injection protection (JPA)
- Input sanitization

## Performance

- Connection pooling (HikariCP)
- Batch operations
- Lazy loading
- Database indexing
- Gzip compression

## Troubleshooting

### Database connection issues
```bash
# Check PostgreSQL is running
pg_isready

# Check connection
psql -U postgres -d mediaupload
```

### AWS S3 issues
```bash
# Test AWS credentials
aws s3 ls s3://your-bucket-name
```

### Port already in use
```bash
# Change port in application.yml
server:
  port: 8081
```
