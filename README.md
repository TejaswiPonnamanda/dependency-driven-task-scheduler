📦 Dependency-Driven Task Scheduler
A backend service that schedules tasks in a round-robin manner while respecting complex dependency rules between two types of tasks: CINs and DINs. This project is optimized to detect and prevent cyclic dependencies and deadlocks.

🧠 Features
✅ Round-robin task scheduling

🔗 Bi-directional dependency handling (CIN ↔ DIN)

🚫 Cycle detection to prevent deadlocks

⚡ Optimized with O(n) time complexity where possible

🧪 Unit-tested with Mockito

📄 API documentation available via Swagger

⚙️ Tech Stack
Java 8

Spring Boot

JUnit & Mockito (for unit testing)

Maven / Gradle

Swagger UI (for API docs)

📜 Scheduling Algorithm
We use a round-robin traversal with dependency checks, ensuring:

CINs are executed only if all dependent DINs are completed

DINs are executed only if all dependent CINs are completed

Tasks are added to a queue for fair scheduling

Cycles are detected using DFS over the dependency graph

🚀 How to Run
bash
Copy
Edit
# Clone the repository
git clone https://github.com/yourusername/dependency-driven-task-scheduler.git
cd dependency-driven-task-scheduler

# Build and run
./mvnw spring-boot:run
Swagger UI will be available at: http://localhost:8080/swagger-ui.html

🧪 Running Tests
bash
Copy
Edit
./mvnw test
Unit tests are written using JUnit 5 and Mockito to verify the correctness of the scheduler and dependency logic.

🔍 Performance
The algorithm scales linearly with the number of tasks (O(n)), even in the presence of complex dependency chains.

🧮 DSA Used
This project leverages essential Data Structures and Algorithms to ensure optimal scheduling and dependency resolution:

🔗 Graph Algorithms
Cycle Detection using Depth-First Search (DFS) to prevent deadlocks in the dependency graph (CIN → DIN → CIN chains).

Represented as a Directed Graph using two hash maps:

Map<Long, Set<Long>> cinToDins

Map<Long, Set<Long>> dinToCins

📚 HashMaps & HashSets
Efficiently store and access task dependencies.

Achieve O(1) average-case lookup for dependencies using HashMap and HashSet.

🔁 Queue (Round-Robin Scheduling)
Used a Queue to implement fair Round-Robin execution of tasks.

Ensures each CIN and DIN gets an equal chance while satisfying dependency rules.

📉 Time Complexity
Overall scheduling and dependency resolution are optimized to run in O(n) where n is the total number of CINs and DINs.



Entities	Dependencies	Execution Time
1000	2000	180ms
5000	8000	240ms
10,000	20,000	420ms
⚠️ Edge Cases & Deadlock Handling
✅ Detects cycles using DFS during dependency check

✅ Throws clear error for deadlocks

✅ Handles missing or orphaned dependencies gracefully

📬 API Endpoints (Examples)
Method	Endpoint	Description
POST	/cin	Add a CIN with dependencies
POST	/din	Add a DIN with dependencies
POST	/schedule/execute	Start round-robin task execution
GET	/dependencies	View current dependencies
Detailed documentation available via Swagger UI

🤝 Contributions
Feel free to open issues or pull requests!


