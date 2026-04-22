# Smart Campus API – Coursework Report by Saeed Abdullah / w17253516

This README contains:
- Answers to the questions in each part of the coursework
- API design overview
- Build & run instructions
- Sample curl commands
- Conceptual report section

```markdown
## Part 1 – Setup & Discovery

### 1.1 Project Setup
I created a new Java project using Maven and added the required Jersey dependencies for building RESTful endpoints.  
The project follows a simple package structure:

- `com.smartcampus.api` → data models  
- `com.smartcampus.api.resources` → REST API resources  

This keeps the code organised and easy to navigate.

### 1.2 Base URL
All endpoints in the API follow the same base path:

/api/v1/

This makes the API predictable and consistent.

### 1.3 Discovery Endpoint
A simple discovery endpoint was implemented to confirm that the API is running and reachable.  
It returns a basic JSON message to show that the server is active.

Example response:
{
  "message": "Smart Campus API is running"
}

---

## Part 2 – Room Management

### 2.1 Room Model
A Room contains four main fields:
- `id`
- `name`
- `capacity`
- `sensorIds` (a list of sensors linked to the room)

This keeps the structure simple and suitable for a small prototype.

### 2.2 Endpoints Implemented
The following REST endpoints were created for managing rooms:

- GET /api/v1/rooms – returns all rooms  
- GET /api/v1/rooms/{id} – returns a specific room  
- POST /api/v1/rooms – creates a new room  
- DELETE /api/v1/rooms/{id} – deletes a room (only if it has no sensors)

These endpoints cover the basic CRUD operations required for the coursework.

### 2.3 Validation
Simple validation rules were added:
- A room cannot be created if the ID already exists.
- A room cannot be deleted if it still has sensors attached.
- Requests for missing rooms return a 404 error.

This ensures the API behaves predictably without adding unnecessary complexity.

---

## Part 3 – Sensors & Filtering

### 3.1 Sensor Model
A Sensor contains:
- `id`
- `type` (e.g., temperature, motion)
- `roomId` (the room the sensor belongs to)

This keeps the structure simple and allows sensors to be linked directly to rooms.

### 3.2 Endpoints Implemented
The following endpoints were added for sensor management:

- GET /api/v1/sensors – returns all sensors  
- GET /api/v1/sensors/{id} – returns a specific sensor  
- POST /api/v1/sensors – creates a new sensor  
- DELETE /api/v1/sensors/{id} – deletes a sensor  

These endpoints provide basic CRUD functionality for sensors.

### 3.3 Filtering
A simple filtering feature was added using:

- GET /api/v1/sensors?type={type}

This allows clients to filter sensors by type (e.g., temperature sensors only).  
The filtering is done in memory and is suitable for a small coursework project.

### 3.4 Validation
Basic validation rules include:
- A sensor cannot be created if the ID already exists.
- A sensor must be linked to an existing room.
- Requests for missing sensors return a 404 error.

These checks help keep the API consistent and prevent invalid data.

---

## Part 4 – Sub‑Resources

### 4.1 Purpose of Sub‑Resources
Sub‑resources were used to represent relationships between rooms and their sensors.  
This keeps the API structure clear and avoids long, complicated URLs.

### 4.2 Endpoints Implemented
The following sub‑resource endpoints were added:

- GET /api/v1/rooms/{id}/sensors – returns all sensors in a room  
- POST /api/v1/rooms/{id}/sensors – adds a new sensor to a room  

These endpoints make it easy to manage sensors directly through their parent room.

### 4.3 Validation
- A room must exist before adding a sensor to it.  
- If a room has no sensors, the GET request returns an empty list.  
- Invalid room IDs return a 404 error.

This keeps the sub‑resource behaviour predictable and consistent.

---

## Part 5 – Error Handling & Logging

### 5.1 Error Handling
Basic error handling was added across the API to make responses consistent and easy to understand.  
The main error cases covered include:

- **404 Not Found** – returned when a room or sensor does not exist  
- **400 Bad Request** – returned when required fields are missing or invalid  
- **409 Conflict** – returned when trying to create a room or sensor with an existing ID  

These responses help clients understand what went wrong without exposing internal details.

### 5.2 Error Response Format
All error messages follow a simple JSON structure:

{
  "error": "Description of the problem"
}

This keeps the API predictable and easy to test.

### 5.3 Logging
Basic console logging was added to track key actions such as:

- Creating rooms and sensors  
- Deleting resources  
- Failed lookups  
- Invalid requests  

The logging is lightweight and suitable for a small coursework project.  
It helps with debugging and makes it easier to follow what the API is doing during testing.

```
---------------------------------------------------------------------------------------------------------------

## Conceptual Report

### Overview
The Smart Campus API was designed as a small, lightweight RESTful service that demonstrates core concepts such as resources, sub‑resources, filtering, validation, and error handling.  
The focus was on keeping the design simple, consistent, and easy to test while still meeting the coursework requirements.

### RESTful Design
The API follows REST principles by:
- Using clear resource names (rooms, sensors)
- Using standard HTTP methods (GET, POST, DELETE)
- Returning JSON responses
- Using meaningful status codes (200, 201, 404, 409)

This makes the API predictable and easy for clients to interact with.

### Resource Structure
Two main resources were used:
- **Rooms** – represent physical spaces
- **Sensors** – represent devices linked to rooms

Sensors are linked to rooms through a simple `roomId` field.  
This keeps the data model easy to understand and avoids unnecessary complexity.

### Sub‑Resources
Sub‑resources were used to show relationships between rooms and sensors:

- /rooms/{id}/sensors

This makes the API more intuitive because clients can access sensors directly through their parent room.  
It also avoids long or confusing URLs.

### Filtering
A basic filtering feature was added:

- /sensors?type={type}

This demonstrates how query parameters can be used to refine results without creating new endpoints.  
The filtering is done in memory, which is acceptable for a small coursework project.

### Validation & Error Handling
Validation was added to prevent invalid data, such as:
- Duplicate IDs
- Linking sensors to non‑existent rooms
- Deleting rooms that still have sensors

Consistent error messages help clients understand what went wrong.

### Logging
Simple console logging was added to track:
- Resource creation
- Deletions
- Failed lookups
- Invalid requests

This helps with debugging and shows awareness of backend development practices.

### Summary
Overall, the API demonstrates:
- Clean REST design
- Clear resource relationships
- Basic filtering
- Proper validation
- Consistent error handling
- Lightweight logging

The implementation is intentionally simple but covers all required concepts for the coursework.

