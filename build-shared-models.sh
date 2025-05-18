#!/bin/bash

# Build and install shared-models
cd shared-models
mvn clean install
cd ..

# Build all services
cd services/notification-service
mvn clean install
cd ../question-service
mvn clean install
cd ../answer-service
mvn clean install
cd ../user-service
mvn clean install
cd ../.. 