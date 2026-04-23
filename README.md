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

1. Get all rooms

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

## Conceptual Report — Answers to Coursework Questions

Q1 — JAX‑RS uses a per‑request lifecycle, meaning a new instance of each resource class is created for every incoming request. This keeps resources stateless and avoids sharing instance fields across requests. Because nothing is stored inside the resource objects themselves, any data that needs to persist must be kept in shared structures such as static maps or lists. The downside is that these shared structures can become a point of contention if multiple threads modify them at the same time. In a real multi‑threaded environment, thread‑safe collections or synchronization would be required to avoid race conditions or data loss.

Q2 — Hypermedia adds links inside responses that guide clients toward related actions, making the API more self‑navigable. Instead of relying entirely on documentation, clients can discover available operations directly from the responses they receive. This reduces the risk of hard‑coded paths and makes the system more flexible if the API evolves. It also improves the overall developer experience by making the API easier to explore and integrate with.

Q3 — Returning only room IDs keeps responses small and reduces bandwidth usage, which can matter in large datasets or limited environments. However, it forces clients to make additional requests to retrieve full details. Returning full room objects provides all relevant information in one response, simplifying client logic and reducing round‑trips. For a small dataset like this coursework, the overhead of returning full objects is minimal, so the more complete response is generally more convenient.

Q4 — In this implementation, DELETE behaves idempotently. The first DELETE removes the room if it exists and is eligible for deletion. Any repeated DELETE requests for the same room simply return a “not found” response, but the overall state of the system does not change. This matches the definition of idempotency, where multiple identical requests have the same final effect as a single request. This behaviour is important when clients retry operations due to network issues.

Q5 — The POST method explicitly consumes JSON. If a client sends data as text/plain or application/xml, JAX‑RS will not match the request to the method because the media type does not satisfy the @Consumes requirement. As a result, the framework automatically returns a 415 Unsupported Media Type response. This prevents the server from attempting to parse incompatible formats and ensures that only valid JSON payloads reach the application logic.

Q6 — Using query parameters for filtering keeps the base URL stable and clearly indicates that filtering is optional. Query parameters also allow multiple filters to be combined naturally, such as type and status. Embedding the filter in the path makes the URL structure more rigid and mixes resource identity with search criteria. Query parameters follow REST conventions for searching and filtering collections and scale better as more filters are added.

Q7 — The Sub‑Resource Locator pattern allows nested resources to be handled by dedicated classes rather than crowding everything into a single controller. This keeps each class focused on a specific part of the API and avoids large, hard‑to‑maintain files. As the API grows, this separation makes it easier to extend functionality without introducing complexity in unrelated areas. It also improves readability and keeps responsibilities clearly divided.

Q8 — A 422 response indicates that the request was well‑formed JSON, but the server could not process it due to a semantic issue, such as referencing a room that does not exist. This is more precise than a 404, which suggests the endpoint itself is missing. In this case, the endpoint is valid; the problem lies inside the payload. Using 422 gives the client clearer feedback about what went wrong and how to correct it.

Q9 — Stack traces reveal internal class names, file paths, and library versions, all of which can be valuable to an attacker. This information can help someone identify outdated dependencies, vulnerable components, or internal implementation details that should remain private. Exposing stack traces also makes it easier to map out the structure of the application. Returning a generic error response instead reduces the attack surface and keeps sensitive details hidden.

Q10 — Logging is a cross‑cutting concern, and filters allow it to be applied consistently across all requests without cluttering individual resource methods. This keeps the resource classes focused on business logic rather than infrastructure tasks. Filters also ensure that logging happens even if new endpoints are added later, without requiring additional code. Overall, this approach leads to cleaner, more maintainable code and a more predictable logging setup.
