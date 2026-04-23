# Conceptual Report — Answers to Coursework Questions Saeed Abdullah / w17253516

## Part 1 — Service Architecture & Setup

### Q1 — JAX‑RS Resource Lifecycle
JAX‑RS uses a **per‑request lifecycle**, meaning a new instance of each resource class is created for every incoming HTTP request. This ensures resource classes remain stateless and thread‑safe by default, because no instance is shared across requests. However, it also means that any data stored in instance fields would be lost immediately after the request finishes. To maintain persistent in‑memory state, shared data structures such as maps and lists must be stored in **static fields**, which are shared across all resource instances. In a real concurrent environment, this design could introduce race conditions, so thread‑safe collections (e.g., `ConcurrentHashMap`) or explicit synchronization would be required. For this coursework, sequential execution makes simple static maps sufficient.

---

### Q2 — Value of Hypermedia (HATEOAS)
Hypermedia (HATEOAS) embeds navigational links directly inside API responses, allowing clients to discover available actions dynamically. This makes the API self‑describing, reducing the need for clients to rely on static documentation or hard‑coded paths. It also allows the server to evolve without breaking clients, because updated links guide them to the correct endpoints. For developers, this improves usability and reduces integration errors, especially in large or frequently changing APIs. Overall, hypermedia enhances flexibility and long‑term maintainability.

---

## Part 2 — Room Management

### Q3 — Returning IDs vs Full Room Objects
Returning only room IDs reduces payload size and network bandwidth, which can be beneficial for large datasets or low‑bandwidth environments. However, it forces clients to make additional requests to retrieve full room details, increasing latency and complexity on the client side. Returning full room objects provides all necessary information in a single response, making the API easier to use and reducing round‑trips. For a small dataset like this coursework, the overhead of returning full objects is negligible. Therefore, returning full objects offers a better balance of clarity and convenience.

---

### Q4 — Idempotency of DELETE
DELETE is idempotent in this implementation because repeating the same DELETE request does not change the final state of the system. The first DELETE removes the room if it has no sensors assigned. Any subsequent DELETE for the same room returns **404 Not Found**, because the room no longer exists, but the system state remains unchanged. This behaviour satisfies the definition of idempotency: multiple identical requests have the same effect as a single request. This is important for reliability, especially when clients retry failed operations.

---

## Part 3 — Sensor Operations & Linking

### Q5 — Wrong Media Type with @Consumes(JSON)
The POST method for creating sensors uses `@Consumes(MediaType.APPLICATION_JSON)`, which tells JAX‑RS to only accept JSON payloads. If a client sends data with a different media type, such as `text/plain` or `application/xml`, the framework cannot match the request to the method. As a result, JAX‑RS automatically returns **415 Unsupported Media Type** without attempting to parse the body. This prevents invalid or incompatible data formats from entering the system. It also ensures consistent request handling and predictable deserialization behaviour.

---

### Q6 — @QueryParam vs Path Segment for Filtering
Using `@QueryParam` for filtering (e.g., `?type=CO2`) is preferred because filters are optional and do not change the identity of the resource collection. Query parameters allow multiple filters to be combined naturally, such as `?type=CO2&status=active`. They also keep the base path (`/sensors`) clean and stable, which improves API consistency. In contrast, embedding filters in the path (e.g., `/sensors/type/CO2`) creates unnecessary URL variations and blurs the distinction between resource identity and search criteria. Query parameters are the standard REST convention for filtering and searching collections.

---

## Part 4 — Sub‑Resources (Theory Only)

### Q7 — Benefits of the Sub‑Resource Locator Pattern
The Sub‑Resource Locator pattern allows nested resources (such as sensor readings) to be handled by dedicated classes rather than crowding the parent resource. This improves separation of concerns, making each class responsible for a specific part of the API. It also reduces complexity in the main resource classes, preventing them from becoming large and difficult to maintain. As APIs grow, this pattern scales better because new nested behaviours can be added without modifying existing controllers. Overall, it leads to cleaner organisation and easier long‑term maintenance.

---

### Q8 — Updating currentValue on New Reading
When a new reading is added for a sensor, updating the sensor’s `currentValue` ensures that the sensor object always reflects its latest state. This avoids situations where the reading history and the sensor’s main record become inconsistent. It also allows clients to quickly retrieve the most recent reading without loading the entire history. This improves performance and simplifies client logic. Maintaining this consistency is important for any API that exposes both historical and real‑time data.

---

## Part 5 — Error Handling, Exception Mapping & Logging

### Q9 — Why 422 vs 404 for Missing Linked Room
When a client POSTs a sensor with a `roomId` that does not exist, the endpoint itself is valid and the JSON structure is correct. The issue is a semantic error inside the payload: it references a resource that is not present. HTTP **422 Unprocessable Entity** accurately communicates that the server understood the request but cannot process it due to invalid semantics. A 404 would incorrectly imply that the endpoint itself does not exist. Therefore, 422 is the more precise and meaningful status code for this scenario.

---

### Q10 — Security Risks of Exposing Stack Traces
Exposing raw Java stack traces reveals internal implementation details such as class names, file paths, frameworks, and library versions. Attackers can use this information to identify vulnerabilities, outdated components, or exploitable patterns. Stack traces may also expose sensitive logic or internal assumptions that should remain hidden. Returning a generic 500 response through a global exception mapper prevents this leakage. This improves security by reducing the attack surface.

---

### Q11 — Why Use JAX‑RS Filters for Logging
JAX‑RS filters allow logging logic to be applied automatically to all incoming requests and outgoing responses. This centralises cross‑cutting concerns, avoiding duplicated `Logger.info()` calls in every resource method. It keeps resource classes clean and focused on business logic rather than infrastructure tasks. Filters also ensure consistent logging behaviour across the entire API. This approach improves maintainability and observability without cluttering the codebase.
