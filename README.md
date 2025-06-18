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
