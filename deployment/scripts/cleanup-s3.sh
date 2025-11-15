#!/bin/bash
# S3 Cleanup Script for Expired Media Files

set -e

# Configuration
AWS_PROFILE="${AWS_PROFILE:-default}"
S3_BUCKET="${AWS_S3_BUCKET}"
LOG_FILE="cleanup_$(date +%Y%m%d_%H%M%S).log"

echo "Starting S3 cleanup for bucket: $S3_BUCKET" | tee -a "$LOG_FILE"

# Note: This script should work in conjunction with database cleanup
# In production, consider using S3 lifecycle policies instead

# List files older than 365 days
aws s3 ls "s3://${S3_BUCKET}/media/" --recursive --profile "$AWS_PROFILE" | \
  awk -v date="$(date -d '365 days ago' +%Y-%m-%d)" '$1 < date {print $4}' | \
  while read -r file; do
    echo "Deleting old file: $file" | tee -a "$LOG_FILE"
    aws s3 rm "s3://${S3_BUCKET}/${file}" --profile "$AWS_PROFILE"
  done

echo "S3 cleanup completed!" | tee -a "$LOG_FILE"
