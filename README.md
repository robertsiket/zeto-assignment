# EDF File Processing Application

This application consists of a Spring Boot backend and an Angular frontend for processing and displaying EDF (European Data Format) files.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
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
