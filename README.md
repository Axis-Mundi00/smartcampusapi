# Smart Campus API — Coursework Submission  
By Saeed Abdullah (w17253516)

---

## 1. Overview of API Design

The Smart Campus API is a small RESTful service built using Java, Maven, and Jersey (JAX‑RS).  
It exposes two main resource collections:

- **Rooms** – representing physical spaces on campus  
- **Sensors** – representing devices assigned to rooms  

Rooms can be created, listed, retrieved, and deleted (with safety checks).  
Sensors can be created, listed, retrieved, and filtered by type.  
All data is stored in simple in‑memory static maps to keep the implementation lightweight.  
Custom exception mappers provide consistent JSON error responses, and a request/response logging filter adds basic observability.

---

## 2. How to Run the Project

1. Open the project in **NetBeans** or any Java‑friendly IDE.  
2. Make sure a recent **JDK (11 or higher)** is installed and selected.  
3. In the project structure, locate the **Main.java** file (this starts the server).  
4. Run **Main.java** from your IDE.  
5. Once running, the API will be available at:

http://localhost:8080/api/v1

---

## 3. Sample curl Commands

### 1. Get all rooms

curl -X GET http://localhost:8080/api/v1/rooms

curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"R101","name":"Lecture Hall","capacity":120}'

curl -X GET http://localhost:8080/api/v1/rooms/R101

curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"S1","type":"temperature","roomId":"R101"}'

curl -X GET "http://localhost:8080/api/v1/sensors?type=temperature"

---------------------------------------------------------------------

4. Conceptual Report — Answers to Coursework Questions
Part 1 — Service Architecture & Setup
Q1 — JAX‑RS Resource Lifecycle
JAX‑RS uses a per‑request lifecycle, meaning a new instance of each resource class is created for every incoming request. This keeps resources stateless and avoids shared mutable state inside resource objects. Because instance fields do not persist, any shared in‑memory data must be stored in static maps/lists so all resource instances can access the same data. In a real concurrent environment, this could lead to race conditions, so thread‑safe collections or synchronization would be needed. For this coursework, simple static maps are sufficient due to sequential execution.

Q2 — Value of Hypermedia (HATEOAS)
Hypermedia adds navigational links inside responses, allowing clients to discover available actions dynamically. This reduces reliance on static documentation and makes the API more self‑describing. It also allows the server to evolve without breaking clients, since updated links guide them to the correct endpoints. For developers, this improves usability and reduces integration mistakes.

Part 2 — Room Management
Q3 — Returning IDs vs Full Room Objects
Returning only IDs keeps responses small and reduces bandwidth, but forces clients to make extra requests to retrieve full details. Returning full objects provides all relevant information in one response, which simplifies client‑side logic. For a small dataset like this coursework, the overhead of returning full objects is minimal. Overall, returning full objects offers a clearer and more convenient experience.

Q4 — Idempotency of DELETE
DELETE is idempotent in this API. The first DELETE removes the room (if allowed), and any repeated DELETE requests return 404 Not Found because the room no longer exists. The system state does not change after the first successful deletion, which satisfies the definition of idempotency. This behaviour is important when clients retry operations.

Part 3 — Sensor Operations & Linking
Q5 — Wrong Media Type with @Consumes(JSON)
The POST method consumes application/json. If a client sends another media type, such as text/plain or application/xml, JAX‑RS cannot match the request to the method. It automatically returns 415 Unsupported Media Type without attempting to parse the payload. This prevents invalid formats from entering the system and keeps request handling predictable.

Q6 — Why Use @QueryParam for Filtering
Query parameters are ideal for filtering because they are optional and can be combined (e.g., ?type=CO2&status=active). They keep the base path stable and clearly indicate that the client is still interacting with the same collection. Embedding filters in the path creates unnecessary URL variations and mixes resource identity with search criteria. Query parameters follow REST conventions for searching and filtering collections.

Part 4 — Sub‑Resources (Theory Only)
Q7 — Benefits of the Sub‑Resource Locator Pattern
The Sub‑Resource Locator pattern delegates nested paths to dedicated classes, reducing complexity in the parent resource. This improves separation of concerns and keeps each class focused on a specific part of the API. It also prevents large “God classes” with too many endpoints. As APIs grow, this pattern scales better and makes maintenance easier.

Q8 — Updating currentValue on New Reading
Updating the sensor’s currentValue ensures the sensor always reflects its latest reading. This avoids inconsistencies between the reading history and the sensor’s main record. It also allows clients to retrieve the most recent value without loading the entire history. This keeps the data model coherent and improves performance.

Part 5 — Error Handling, Exception Mapping & Logging
Q9 — Why 422 Instead of 404
When a POST payload references a non‑existent room, the endpoint is valid and the JSON structure is correct. The issue is a semantic error inside the payload. HTTP 422 Unprocessable Entity communicates that the server understands the request but cannot process it due to invalid semantics. A 404 would incorrectly imply the endpoint itself does not exist.

Q10 — Security Risks of Exposing Stack Traces
Stack traces reveal internal class names, file paths, frameworks, and library versions. Attackers can use this information to identify vulnerabilities or outdated components. Exposing stack traces increases the attack surface and can leak sensitive implementation details. A global exception mapper prevents this by returning a safe, generic 500 response.

Q11 — Why Use JAX‑RS Filters for Logging
JAX‑RS filters apply logging logic automatically to all requests and responses. This avoids duplicating logging code in every resource method and keeps resource classes focused on business logic. Filters ensure consistent observability across the entire API and centralize cross‑cutting concerns. This leads to cleaner, more maintainable code.

