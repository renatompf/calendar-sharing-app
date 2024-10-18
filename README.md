
# Calendar Sharing Application

## Overview

The Calendar Sharing Application is a RESTful web service that allows users to create, manage, and share calendars and events. Users can register, log in, and perform various operations on their calendars, including sharing with other users and managing events.

## Features

- User registration and authentication
- Calendar creation, retrieval, and deletion
- Event management including creation, retrieval, and deletion
- Sharing calendars with other users
- Email verification for account security
- Asynchronous email delivery using RabbitMQ
- MailDev for local mail server

## Technology Stack

- [Java](https://www.oracle.com/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
  - Spring Data JPA
  - Spring Security
  - Spring Mail
- [Hibernate](https://hibernate.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Flyway](https://www.red-gate.com/products/flyway/)
- [RabbitMQ](https://www.rabbitmq.com/) for asynchronous email delivery
- [Maildev](https://maildev.github.io/maildev/)
- [Docker](https://www.docker.com/)

## Installation

### Prerequisites

- Docker

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/calendar-sharing-app.git
   cd calendar-sharing-app
   ```

2. Run the application:
   ```bash
   docker-compose up --build;
   ```

3. The application will be available at `http://localhost:8080`.

## API Endpoints

### Authentication Endpoints

- **Register User**
    - `POST /auth/register`
    - Request Body:
      ```json
      {
        "firstName": "Ada",
        "lastName": "Lovelace",
        "email": "ada.lovelace@computer.org",
        "birthDate": "1815-12-10",
        "password": "algorithms123"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{
        "firstName": "Ada",
        "lastName": "Lovelace",
        "email": "ada.lovelace@computer.org",
        "birthDate": "1815-12-10",
        "password": "algorithms123"
      }'
      ```

- **Verify Email**
    - `GET /auth/verify-email?token=your_token`
    - **Curl Command:**
      ```bash
      curl -X GET "http://localhost:8080/auth/verify-email?token=your_token"
      ```
    **NOTE: You can access to the maildev container - running on http://localhost:1080 - and accept the token there**


- **Login User**
    - `POST /auth/login`
    - Request Body:
      ```json
      {
        "email": "ada.lovelace@computer.org",
        "password": "algorithms123"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{
        "email": "ada.lovelace@computer.org",
        "password": "algorithms123"
      }'
      ```

- **Refresh Token**
    - `GET /auth/refresh-token`
    - **Curl Command:**
      ```bash
      curl -X GET http://localhost:8080/auth/refresh-token
      ```

### Calendar Endpoints

- **Get All Calendars**
    - `GET /calendars`
    - **Curl Command:**
      ```bash
      curl -X GET http://localhost:8080/calendars
      ```

- **Get Calendar By ID**
    - `GET /calendars/{calendarId}`
    - **Curl Command:**
      ```bash
      curl -X GET http://localhost:8080/calendars/{calendarId}
      ```

- **Create Calendar**
    - `POST /calendars`
    - Request Body:
      ```json
      {
        "name": "AI Development Calendar"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X POST http://localhost:8080/calendars -H "Content-Type: application/json" -d '{
        "name": "AI Development Calendar"
      }'
      ```

- **Delete Calendar**
    - `DELETE /calendars/{calendarId}`
    - **Curl Command:**
      ```bash
      curl -X DELETE http://localhost:8080/calendars/{calendarId}
      ```

### Calendar Sharing Endpoints

- **Share Calendar**
    - `POST /calendar-sharing`
    - Request Body:
      ```json
      {
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c",
        "sharedWith": "charles.babbage@computer.org",
        "calendarPermissions": "READ_WRITE"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X POST http://localhost:8080/calendar-sharing -H "Content-Type: application/json" -d '{
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c",
        "sharedWith": "charles.babbage@computer.org",
        "calendarPermissions": "READ_WRITE"
      }'
      ```

- **Validate Sharing Token**
    - `GET /calendar-sharing/token-validation?token=your_token`
    - **Curl Command:**
      ```bash
      curl -X GET "http://localhost:8080/calendar-sharing/token-validation?token=your_token"
      ```

    **NOTE: You can access to the maildev container - running on http://localhost:1080 - and accept this invitation there**


- **Delete Calendar Sharing**
    - `DELETE /calendar-sharing`
    - Request Body:
      ```json
      {
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c",
        "email": "charles.babbage@computer.org"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X DELETE http://localhost:8080/calendar-sharing -H "Content-Type: application/json" -d '{
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c",
        "email": "charles.babbage@computer.org"
      }'
      ```

### Event Endpoints

- **Get All Events for a Calendar**
    - `GET /events/calendar/{calendarId}?startDate={startDate}&endDate={endDate}`
    - **Curl Command:**
      ```bash
      curl -X GET "http://localhost:8080/events/calendar/{calendarId}?startDate={startDate}&endDate={endDate}"
      ```

- **Get Event by ID**
    - `GET /events/{eventId}`
    - **Curl Command:**
      ```bash
      curl -X GET http://localhost:8080/events/{eventId}
      ```

- **Create Event**
    - `POST /events`
    - Request Body:
      ```json
      {
        "title": "Tech Conference 2024",
        "description": "Annual tech conference for developers.",
        "startTime": "2024-10-20T10:00:00",
        "endTime": "2024-10-20T18:00:00",
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c"
      }
      ```
    - **Curl Command:**
      ```bash
      curl -X POST http://localhost:8080/events -H "Content-Type: application/json" -d '{
        "title": "Tech Conference 2024",
        "description": "Annual tech conference for developers.",
        "startTime": "2024-10-20T10:00:00",
        "endTime": "2024-10-20T18:00:00",
        "calendarId": "b90b1e80-1748-4419-bbf4-2360e2d1c61c"
      }'
      ```

- **Delete Event**
    - `DELETE /events/{eventId}`
    - **Curl Command:**
      ```bash
      curl -X DELETE http://localhost:8080/events/{eventId}
      ```

### Shared Calendars Endpoints

- **Get All Shared Calendars**
    - `GET /shared-calendars`
    - **Curl Command:**
      ```bash
      curl -X GET http://localhost:8080/shared-calendars
      ```
