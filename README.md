# Smart Campus API — Coursework Submission  
By Saeed Abdullah (w17253516)

---

## 1. Overview of API Design

The Smart Campus API is a lightweight RESTful service built using Java, Maven, and Jersey (JAX‑RS).  
It exposes two main resource collections:

- **Rooms** – representing physical spaces on campus  
- **Sensors** – representing devices assigned to rooms  

Rooms can be created, listed, retrieved, and deleted (with checks to prevent deleting rooms that still have sensors).  
Sensors can be created, listed, retrieved, and filtered by type.  
All data is stored in simple in‑memory static maps to keep the implementation straightforward for coursework requirements.  
Custom exception mappers provide consistent JSON error responses, and a request/response logging filter adds basic observability.

---

## 2. How to Run the Project

1. Open the project in **NetBeans** or any Java‑friendly IDE.  
2. Make sure a recent **JDK (11 or higher)** is installed and selected.  
3. In the project tree, locate the **Main.java** file (this is the server entry point).  
4. Run **Main.java** from your IDE.  
5. Once the server starts, the API will be available at:

http://localhost:8080/api/v1


---

## 3. Sample curl Commands

### 1. Get all rooms
```bash
curl -X GET http://localhost:8080/api/v1/rooms

2. Create a new room
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"R101","name":"Lecture Hall","capacity":120}'

3. Get a specific room
curl -X GET http://localhost:8080/api/v1/rooms/R101

4. Create a new sensor
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S1","type":"temperature","roomId":"R101"}'

5. Filter sensors by type
curl -X GET "http://localhost:8080/api/v1/sensors?type=temperature"
