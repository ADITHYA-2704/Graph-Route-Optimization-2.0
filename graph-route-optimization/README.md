# Graph-Based Route Optimization System

![Java](https://img.shields.io/badge/Java-24-orange)
![Maven](https://img.shields.io/badge/Build-Maven-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL_8.0-4479A1)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Active-brightgreen)

A Java-based backend system that models a real-world transport network as a **weighted graph** — cities as vertices, routes as weighted directed edges — and computes optimal paths between them using classical **Data Structures and Algorithms**, backed by a normalized **MySQL** database via **JDBC**. Built to demonstrate practical application of graph theory, object-oriented design, layered software architecture, and relational database design in a complete, working Java application.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Algorithms Used](#algorithms-used)
- [Database Design](#database-design)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [Future Enhancements](#future-enhancements)
- [License](#license)
- [Author](#author)

---

## Project Overview

The **Graph-Based Route Optimization System** represents a city road network as an **adjacency-list graph** (`Map<String, List<Route>>`) and provides a menu-driven console application for building, querying, and persisting that network. It supports multiple interchangeable shortest-path algorithms via the **Strategy design pattern**, persists data through a **DAO (Data Access Object)** layer over JDBC, and exposes a clean **Service Layer** that keeps the console UI decoupled from both the database and the algorithm implementations.

This project was built to demonstrate, in a single working codebase:
- Practical **graph theory** and **algorithm design** (Dijkstra's Algorithm, A* Search, Bellman-Ford Algorithm, BFS, DFS)
- **Object-Oriented Programming** principles: encapsulation, abstraction, immutability, defensive copying
- **Software design patterns**: Strategy Pattern, DAO Pattern, Service Layer Pattern
- **Relational database design**: normalization (3NF), primary/foreign keys, constraints, indexing
- **JDBC** connectivity with **connection pooling** (HikariCP)
- Clean **layered architecture** and **exception handling** in a production-style Java project

---

## Features

- **City Management** — add, remove, and search cities in the network
- **Route Management** — add directed or bidirectional weighted routes; remove or update existing routes
- **Shortest Path Computation** — swap between four algorithms at runtime (Dijkstra, A*, Bellman-Ford, DFS) without changing any calling code
- **Graph Traversal** — Breadth-First Search and Depth-First Search reachability queries
- **Persistent Storage** — full CRUD against a MySQL database via a dedicated DAO layer
- **Connection Pooling** — HikariCP-backed connection management for efficient database access
- **Input Validation** — centralized validation across every user-facing operation
- **Custom Exception Handling** — dedicated exception types (`GraphException`, `DatabaseConnectionException`, `RouteNotFoundException`) for meaningful, recoverable error handling
- **Menu-Driven Console Interface** — clean, loop-driven CLI with input validation and graceful error recovery

---

## Technologies Used

| Category | Technology |
|---|---|
| Language | Java 24 |
| Build Tool | Apache Maven |
| Database | MySQL 8.0 |
| Connectivity | JDBC (MySQL Connector/J) |
| Connection Pooling | HikariCP |
| Testing | JUnit 5 |
| Logging | SLF4J + Logback |
| IDE | IntelliJ IDEA |
| Version Control | Git / GitHub |
| Database Tooling | MySQL Workbench |

---

## Algorithms Used

| Algorithm | Time Complexity | Handles Negative Weights | Notes |
|---|---|---|---|
| **Dijkstra's Algorithm** | O((V + E) log V) | No | Guaranteed shortest path; implemented with a binary-heap `PriorityQueue` |
| **A\* Search** | O((V + E) log V) | No | Same guarantee as Dijkstra, with a pluggable heuristic function |
| **Bellman-Ford Algorithm** | O(V · E) | Yes | Detects and reports negative-weight cycles |
| **Breadth-First Search (BFS)** | O(V + E) | N/A | Reachability analysis using a `Queue`; ignores edge weights |
| **Depth-First Search (DFS)** | O(V + E) | N/A | Recursive traversal for reachability and path discovery |

All shortest-path algorithms implement a common `ShortestPathAlgorithm` interface (**Strategy Design Pattern**), allowing the algorithm used at runtime to be selected dynamically without modifying any client code.

---

## Database Design

The system uses a **normalized (3NF)** MySQL schema of five tables:

| Table | Purpose |
|---|---|
| `cities` | Vertices of the graph — one row per city |
| `routes` | Directed, weighted edges between cities (distance in km) |
| `traffic` | Time-stamped congestion/delay readings per route |
| `users` | Application user accounts |
| `saved_routes` | User-saved shortest-path computations, with algorithm used and total distance |

**Design highlights:**
- Surrogate primary keys (`AUTO_INCREMENT`) on every table
- Foreign keys with `ON DELETE CASCADE` to maintain referential integrity
- `CHECK` constraints (e.g. no self-loop routes, positive distances) enforced at the database level
- `UNIQUE` constraints preventing duplicate routes between the same city pair
- Indexes on frequently queried columns (e.g. `destination_city_id`) for efficient lookups

Full schema definition, constraints, and seed data are in [`sql/schema.sql`](./sql/schema.sql).

---

## Project Structure

```
com.routeoptimizer
├── algorithm      # Dijkstra, A*, Bellman-Ford, BFS, DFS (Strategy Pattern)
├── config         # Application & database configuration (.properties loading)
├── dao            # JDBC Data Access Objects (CityDAO, RouteDAO)
├── exception      # Custom exception types
├── graph          # Core Graph data structure (adjacency list)
├── model          # Domain entities: City, Route, PathResult
├── service        # Service Layer (RouteService) - orchestrates dao + graph + algorithm
├── ui             # Console-based menu-driven interface
└── util           # Shared helpers (connection pooling, validation, constants)
```

---

## Installation

### Prerequisites
- JDK 24
- Apache Maven
- MySQL Server 8.0
- Git

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/graph-route-optimization.git
   cd graph-route-optimization
   ```

2. **Set up the database**
   ```bash
   mysql -u root -p < sql/schema.sql
   ```

3. **Configure database credentials**

   Edit `src/main/resources/db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/route_optimizer_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=your_password_here
   db.driver=com.mysql.cj.jdbc.Driver
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.routeoptimizer.Main"
   ```
   or run `Main.java` directly from IntelliJ IDEA.

---

## Usage

On launch, the application loads existing cities and routes from the database and presents a menu-driven console interface:

```
========================================
   Graph-Based Route Optimization System
========================================
 1. Add City
 2. Add Route
 3. Remove Route
 4. Display Graph
 5. Find Shortest Path
 6. BFS (Breadth-First Search)
 7. DFS (Depth-First Search)
 8. Save Database
 9. Exit
========================================
Choose an option:
```

Select an option, follow the prompts, and the result (a computed path, a list of reachable cities, or a confirmation message) is printed directly to the console.

---

## Screenshots

## 📸 Application Interface & Visualizer

### Landing Page
![Landing Page](images/LandingPage.png)

### Route Parameters & Dashboard
![Route Dashboard](images/Route.png)

### Multi-Algorithm Selection Engine
![Algorithm Selection](images/AlgorithmSelection.png)

### Live Route Optimization & Traversal Results
![Working Results](images/Working.png)
---

## Future Enhancements

- [ ] Graphical User Interface (JavaFX or a web-based frontend)
- [ ] REST API layer for programmatic access
- [ ] User authentication and role-based access control
- [ ] Real coordinate-based heuristic for A* (currently a zero-heuristic placeholder)
- [ ] Real-time, traffic-aware dynamic route weighting
- [ ] Docker containerization for simplified deployment
- [ ] CI/CD pipeline (GitHub Actions) for automated build and test
- [ ] Caching layer for frequently requested routes

---

## License

This project is licensed under the **MIT License** — see the [LICENSE](./LICENSE) file for details.

---

## Author

**[ADITHYA ACHARYA]**
[GitHub]([https://github.com/ADITHYA-2704](https://github.com/ADITHYA-2704)) · [LinkedIn](www.linkedin.com/in/adithya-acharya-279612382) · [Email](adithyaacharya004@gmail.com)

---

*If you found this project useful, consider giving it a ⭐ on GitHub.*