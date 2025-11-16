#!/bin/bash
# Database Backup Script for Media QR Upload Application

set -e

# Configuration
BACKUP_DIR="${BACKUP_DIR:-./backups}"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="mediaupload_backup_${TIMESTAMP}.sql"
DB_CONTAINER="${DB_CONTAINER:-media-qr-postgres}"
DB_NAME="${DB_NAME:-mediaupload}"
DB_USER="${DB_USER:-postgres}"

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

echo "Starting database backup..."
echo "Timestamp: $TIMESTAMP"
echo "Backup file: $BACKUP_FILE"

# Perform backup
docker exec -t "$DB_CONTAINER" pg_dump -U "$DB_USER" "$DB_NAME" > "$BACKUP_DIR/$BACKUP_FILE"

# Compress backup
gzip "$BACKUP_DIR/$BACKUP_FILE"

echo "Backup completed successfully!"
echo "Location: $BACKUP_DIR/${BACKUP_FILE}.gz"

# Clean up old backups (keep last 7 days)
find "$BACKUP_DIR" -name "mediaupload_backup_*.sql.gz" -mtime +7 -delete
echo "Old backups cleaned up (kept last 7 days)"
