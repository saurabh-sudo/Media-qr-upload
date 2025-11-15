#!/bin/bash
# AWS Deployment Script for Media QR Upload Application

set -e

# Configuration
STACK_NAME="${STACK_NAME:-media-qr-upload-stack}"
REGION="${AWS_REGION:-us-east-1}"
TEMPLATE_FILE="cloudformation-template.yml"
EC2_KEY_NAME="${EC2_KEY_NAME}"
DB_PASSWORD="${DB_PASSWORD}"
S3_BUCKET="${AWS_S3_BUCKET}"

echo "========================================="
echo "Media QR Upload - AWS Deployment"
echo "========================================="
echo "Stack Name: $STACK_NAME"
echo "Region: $REGION"
echo ""

# Validate required parameters
if [ -z "$EC2_KEY_NAME" ]; then
    echo "Error: EC2_KEY_NAME is required"
    exit 1
fi

if [ -z "$DB_PASSWORD" ]; then
    echo "Error: DB_PASSWORD is required"
    exit 1
fi

if [ -z "$S3_BUCKET" ]; then
    echo "Error: AWS_S3_BUCKET is required"
    exit 1
fi

# Validate CloudFormation template
echo "Validating CloudFormation template..."
aws cloudformation validate-template \
    --template-body file://"$TEMPLATE_FILE" \
    --region "$REGION"

echo "Template validation successful!"
echo ""

# Deploy stack
echo "Deploying CloudFormation stack..."
aws cloudformation create-stack \
    --stack-name "$STACK_NAME" \
    --template-body file://"$TEMPLATE_FILE" \
    --parameters \
        ParameterKey=KeyName,ParameterValue="$EC2_KEY_NAME" \
        ParameterKey=DBPassword,ParameterValue="$DB_PASSWORD" \
        ParameterKey=S3BucketName,ParameterValue="$S3_BUCKET" \
    --capabilities CAPABILITY_IAM \
    --region "$REGION"

echo "Stack creation initiated!"
echo "Waiting for stack to complete..."

# Wait for stack creation
aws cloudformation wait stack-create-complete \
    --stack-name "$STACK_NAME" \
    --region "$REGION"

echo ""
echo "========================================="
echo "Deployment Complete!"
echo "========================================="

# Get stack outputs
aws cloudformation describe-stacks \
    --stack-name "$STACK_NAME" \
    --region "$REGION" \
    --query 'Stacks[0].Outputs' \
    --output table
