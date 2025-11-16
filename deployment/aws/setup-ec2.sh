#!/bin/bash
# EC2 Setup Script for Media QR Upload Application
# Run this script on a fresh EC2 instance

set -e

echo "========================================="
echo "Setting up Media QR Upload on EC2"
echo "========================================="

# Update system
echo "Updating system packages..."
sudo yum update -y

# Install Docker
echo "Installing Docker..."
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Install Docker Compose
echo "Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Git
echo "Installing Git..."
sudo yum install -y git

# Install Java (for backend development)
echo "Installing Java 17..."
sudo yum install -y java-17-amazon-corretto-devel

# Install Node.js (for frontend development)
echo "Installing Node.js..."
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install -y nodejs

# Create application directory
echo "Creating application directory..."
sudo mkdir -p /opt/media-qr-upload
sudo chown ec2-user:ec2-user /opt/media-qr-upload

echo ""
echo "========================================="
echo "EC2 Setup Complete!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Clone the repository to /opt/media-qr-upload"
echo "2. Configure environment variables in .env file"
echo "3. Run: docker-compose up -d"
echo ""
echo "Log out and log back in for Docker group changes to take effect"
