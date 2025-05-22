#!/bin/bash

# Print a header
echo "Starting to apply stateless Kubernetes services..."

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Define the service directories
SERVICE_DIRS=(
    "question"
    "user"
    "answer"
    "notification"
    "apigateway"
)

# Apply each service
for dir in "${SERVICE_DIRS[@]}"; do
    echo "Applying services from $dir..."
    find "$SCRIPT_DIR/$dir" -type f -name "*.yaml" | while read -r file; do
        echo "Applying $file..."
        kubectl apply -f "$file"

        # Check if the command was successful
        if [ $? -eq 0 ]; then
            echo "Successfully applied $file"
        else
            echo "Failed to apply $file"
            exit 1
        fi
    done
done

echo "All services have been applied!" 