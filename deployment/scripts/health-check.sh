#!/bin/bash
# Health Check Script for Media QR Upload Application

set -e

# Configuration
BACKEND_URL="${BACKEND_URL:-http://localhost:8080}"
FRONTEND_URL="${FRONTEND_URL:-http://localhost:80}"

echo "Performing health checks..."
echo "================================"

# Check Backend
echo "Checking backend at: $BACKEND_URL/api/health"
if curl -f -s "$BACKEND_URL/api/health" > /dev/null; then
    echo "✓ Backend is healthy"
    curl -s "$BACKEND_URL/api/health" | jq '.'
else
    echo "✗ Backend health check failed"
    exit 1
fi

echo ""

# Check Frontend
echo "Checking frontend at: $FRONTEND_URL"
if curl -f -s "$FRONTEND_URL" > /dev/null; then
    echo "✓ Frontend is healthy"
else
    echo "✗ Frontend health check failed"
    exit 1
fi

echo ""

# Check Database (if running in Docker)
if docker ps | grep -q media-qr-postgres; then
    echo "Checking database..."
    if docker exec media-qr-postgres pg_isready -U postgres > /dev/null 2>&1; then
        echo "✓ Database is healthy"
    else
        echo "✗ Database health check failed"
        exit 1
    fi
fi

echo ""
echo "================================"
echo "All health checks passed!"
