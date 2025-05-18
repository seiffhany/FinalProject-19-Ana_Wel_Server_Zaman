#!/bin/bash

# chmod +x build-and-push.sh && ./build-and-push.sh
# Build and push user-service
cd user-service
mvn clean package -DskipTests
docker build -t seiffhany/user-service:latest .
docker push seiffhany/user-service:latest
cd ..

# Build and push question-service
cd question-service
mvn clean package -DskipTests
docker build -t seiffhany/question-service:latest .
docker push seiffhany/question-service:latest
cd ..

# Build and push answer-service
cd answer-service
mvn clean package -DskipTests
docker build -t seiffhany/answer-service:latest .
docker push seiffhany/answer-service:latest
cd ..

# Build and push notification-service
cd notification-service
mvn clean package -DskipTests
docker build -t seiffhany/notification-service:latest .
docker push seiffhany/notification-service:latest
cd .. 