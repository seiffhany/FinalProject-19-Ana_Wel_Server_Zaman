# Quora/Piazza Clone - Massive Scalable Applications Project

## Overview

This project is a scalable Q&A platform inspired by Quora and Piazza, developed as part of the "Architecture of Massively Scalable Applications" course (Spring 2025) at German University in Cairo. The system enables users to register, ask questions, provide answers, vote on content, and receive notifications, all while handling high traffic using a microservices architecture.

**Team**: Team 19 - Ana Wel Server Zaman  
**Scrum Master**: Seif Hany  
**Sub-Scrum Masters**: Mostafa Sohile, Seif Hossam, Sara Wasfy  
**Submission Deadline**: May 19, 2025

## Architecture

The application follows a microservices architecture with the following components:

- **User Service**: Manages user registration, JWT-based authentication, profile management, and social interactions (follow/unfollow).
- **Question Service**: Handles question posting, searching (by tags/keywords), upvoting/downvoting, and view tracking.
- **Answer Service**: Manages answer posting, replies, marking as accepted, upvoting/downvoting, and filtering.
- **Notification Service**: Delivers multi-channel notifications (in-app, email) for events like new answers or votes, with archiving support.
- **API Gateway**: Routes client requests to appropriate microservices, handles authentication, and enforces rate limiting.
- **Discovery Service**: Runs a Eureka server for service discovery, enabling dynamic communication between microservices.

Each microservice is independently deployable, containerized using Docker, and orchestrated with Kubernetes, ensuring scalability and loose coupling.

## Key Features

- **User Management**: Register, login (JWT), edit profiles, follow/unfollow users, view profiles.
- **Question Handling**: Post questions, search by tags/keywords, upvote/downvote, track views.
- **Answer Interaction**: Post answers, reply, mark as accepted, upvote/downvote, filter answers.
- **Notifications**: Multi-channel delivery (in-app, email), archive notifications, user-specific notifications.
- **Scalability**: Load balancing (NGINX), service discovery (Eureka), asynchronous processing (RabbitMQ), caching (Redis).

## Technology Stack

- **Backend**: Spring Boot 3.x (Java)
- **Databases**:
  - User & Question Services: PostgreSQL (SQL)
  - Answer & Notification Services: MongoDB (NoSQL)
- **Caching**: Redis
- **Messaging**: RabbitMQ (asynchronous communication for notifications)
- **Service Discovery**: Eureka
- **API Gateway**: Spring Cloud Gateway
- **Containerization**: Docker, Docker Compose
- **Orchestration**: Kubernetes
- **Monitoring & Logging**: Prometheus, Grafana, Loki
- **Testing**: JUnit, Testcontainers, Spring Test

## Dependencies

- **User Service**: Spring Web, Spring Data JPA, PostgreSQL, Spring Security (JWT), Spring Cloud Eureka Client, Spring Data Redis, Validation, Testcontainers.
- **Question Service**: Spring Web, Spring Data JPA, PostgreSQL, Spring Cloud Eureka Client, Spring Data Redis, Validation, Testcontainers.
- **Answer Service**: Spring Web, Spring Data MongoDB, Spring AMQP (RabbitMQ), Spring Cloud Eureka Client, Spring Data Redis, Validation, Testcontainers.
- **Notification Service**: Spring Web, Spring Data MongoDB, Spring AMQP (RabbitMQ), Spring Cloud Eureka Client, Spring Data Redis, Validation, Testcontainers.
- **API Gateway**: Spring Cloud Gateway MVC, Spring Cloud Eureka Client.
- **Discovery Service**: Spring Web, Spring Cloud Eureka Server.

Additional dependencies include `jjwt` (for JWT handling), `loki-logback-appender` (for logging), and testing libraries (e.g., Reactor Test, Spring Security Test, Spring Rabbit Test).

## Project Structure

```
.
├── README.md
├── answer-service
│   └── Dockerfile
├── api-gateway
├── deployment
├── discovery-service
├── notification-service
│   └── Dockerfile
├── question-service
│   └── Dockerfile
└── user-service
    └── Dockerfile
```

## Setup Instructions

1. **Clone the Repository**:
   ```
   git clone https://github.com/your-repo/FinalProject-19-AnaWelServerZaman.git
   cd FinalProject-19-AnaWelServerZaman
   ```
2. **Prerequisites**:
   - Java 17
   - Maven
   - Docker & Docker Compose
   - Kubernetes (e.g., Minikube for local deployment)
   - Redis, RabbitMQ, PostgreSQL, MongoDB (via Docker Compose)
3. **Run Locally with Docker Compose**:
   - Start infrastructure services (databases, Redis, RabbitMQ, Eureka):
     ```
     docker-compose -f deployment/docker-compose.yml up -d
     ```
   - Build and run each service:
     ```
     mvn clean install
     docker-compose -f deployment/docker-compose.yml up --build
     ```
4. **Access the Application**:
   - API Gateway: `http://localhost:8080`
   - Eureka Dashboard: `http://localhost:8761`
5. **Deploy to Kubernetes**:
   - Apply Kubernetes manifests:
     ```
     kubectl apply -f deployment/kubernetes/
     ```

## Future Improvements

- Implement CI/CD pipelines using GitHub Actions or Jenkins.
- Add advanced monitoring dashboards in Grafana.
- Enhance security with rate limiting and input sanitization at the API Gateway.
