#!/bin/bash

# Print a header
echo "Starting to apply stateful Kubernetes resources..."

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Define the stateful resources directories
STATEFUL_DIRS=(
    "postgres-question"
    "postgres-user"
    "mongo-answer"
    "mongo-notification"
    "rabbitmq"
    "redis"
)

# Apply each stateful resource
for dir in "${STATEFUL_DIRS[@]}"; do
    echo "Applying stateful resources from $dir..."
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

echo "All stateful resources have been applied!" 