#!/bin/bash

# Exit on error
set -e

# Function to build and push a service
build_and_push_service() {
    local service_name=$1
    local version=$2

    echo "Processing $service_name..."
    echo "Changing to $service_name directory..."
    cd services/$service_name

    echo "Cleaning target directory and building with Maven..."
    rm -rf target/
    mvn clean package

    echo "Building $service_name Docker image..."
    docker build -t $service_name:$version .

    echo "Tagging $service_name image..."
    docker tag $service_name:$version seiffhany/$service_name:$version

    echo "Verifying image exists..."
    docker images seiffhany/$service_name

    echo "Pushing image to Docker Hub..."
    docker push seiffhany/$service_name:$version

    echo "$service_name build and push completed successfully!"
    cd ../..
}

# List of services to process
services=(
    # "question-service"
    "answer-service"
    "notification-service"
    "user-service"
    "api-gateway"
)

# Version for all services
VERSION="1.0"

# Process each service
for service in "${services[@]}"; do
    build_and_push_service "$service" "$VERSION"
done

echo "All services have been built and pushed successfully!"