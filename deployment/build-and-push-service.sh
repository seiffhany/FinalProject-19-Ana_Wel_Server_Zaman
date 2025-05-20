#!/bin/bash

# Exit on error
set -e

# Check if service name is provided
if [ $# -eq 0 ]; then
    echo "Error: No service name provided"
    echo "Usage: $0 <service-name>"
    echo "Example: $0 question-service"
    exit 1
fi

# Get service name from command line argument
SERVICE_NAME=$1
VERSION="latest"

echo "Processing $SERVICE_NAME..."

# Determine the service directory based on service name
if [ "$SERVICE_NAME" = "api-gateway" ]; then
    SERVICE_DIR="api-gateway"
else
    SERVICE_DIR="services/$SERVICE_NAME"
fi

# Check if service directory exists
if [ ! -d "$SERVICE_DIR" ]; then
    echo "Error: Service directory '$SERVICE_DIR' does not exist"
    exit 1
fi

echo "Changing to $SERVICE_NAME directory..."
cd $SERVICE_DIR

echo "Cleaning target directory and building with Maven..."
rm -rf target/
mvn clean package

echo "Building $SERVICE_NAME Docker image..."
docker build -t $SERVICE_NAME:$VERSION .

echo "Tagging $SERVICE_NAME image..."
docker tag $SERVICE_NAME:$VERSION seiffhany/$SERVICE_NAME:$VERSION

echo "Verifying image exists..."
docker images seiffhany/$SERVICE_NAME

echo "Pushing image to Docker Hub..."
docker push seiffhany/$SERVICE_NAME:$VERSION

echo "$SERVICE_NAME build and push completed successfully!"
cd ../.. 