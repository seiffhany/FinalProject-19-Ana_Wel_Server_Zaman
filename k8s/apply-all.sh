#!/bin/bash

# Print a header
echo "Starting to apply all Kubernetes manifests..."

# Find all .yaml files in the current directory and subdirectories
# and apply them using kubectl
find . -type f -name "*.yaml" | while read -r file; do
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

echo "All manifests have been applied!" 