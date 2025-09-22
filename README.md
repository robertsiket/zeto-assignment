# EDF File Processing Application

This application consists of a Spring Boot backend and an Angular frontend for processing and displaying EDF (European Data Format) files.

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Node.js 18.x or higher
- npm (Node Package Manager)
- Maven 3.8.x or higher

## Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend server will start on `http://localhost:8080`

## Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The frontend application will be available at `http://localhost:4200`

## API Documentation (Swagger/OpenAPI)

The backend is documented using OpenAPI (Swagger) and includes an interactive Swagger UI.

- Start the backend (see steps above) and open Swagger UI:
    - Swagger UI: http://localhost:8080/swagger-ui.html
        - If the above does not redirect, try: http://localhost:8080/swagger-ui/index.html
    - Raw OpenAPI JSON: http://localhost:8080/v3/api-docs

Notes:

- Endpoints are grouped and described with summaries in the UI.
- You can try requests directly from the browser using the Try it out button.
- The API definition is generated automatically at runtime; no extra build steps are required.

## Build Docker image with Maven

This project is configured to build a container image that serves the Angular frontend from the Spring Boot backend jar.

Prerequisites:

- Docker Desktop (or any Docker daemon) running
  - On Windows, ensure Docker Desktop is set to use Linux containers.
- Maven installed

Build steps:

1. Run build image from the backend directory
   ```bash
   cd backend
   mvn clean package spring-boot:build-image
   ```
2. The built image name is `zeto-assignment:0.0.1-SNAPSHOT` (derived from the backend version). Run it with:
   ```bash
   docker run --rm -p 8080:8080 zeto-assignment:0.0.1-SNAPSHOT
   ```

The application will be available at http://localhost:8080 and will serve the Angular frontend at the root path.
