# 🃏 Blackjack WebFlux API

A reactive Blackjack game API using Spring WebFlux, R2DBC with MySQL, MongoDB for game state, and Swagger/OpenAPI for documentation. Supports player registration, game creation, move execution, and player rankings.

---

## 📦 Tech Stack

- **Java 21**
- **Spring Boot 3.2.4**
- **Spring WebFlux**
- **R2DBC (MySQL)** for Player persistence
- **MongoDB (Reactive)** for Game state
- **Maven** for dependency and build management
- **Lombok** for boilerplate reduction
- **OpenAPI/Swagger** for API documentation
- **Docker Compose** for local DB setup
- **JUnit 5**, **Mockito**, **Reactor Test** for testing

---

### 📁 Project Structure
<pre> 
S05T01N01/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cat/itacademy/s05/t01/n01/S05T01N01/
│   │   │       ├── controllers/                      # REST controllers for Player and Game APIs
│   │   │       ├── DTOs/
│   │   │       │   ├── requests/                     # Request DTOs
│   │   │       │   ├── responses/                    # Response DTOs
│   │   │       │   └── mappers/                      # DTO mappers and converters
│   │   │       ├── exceptions/                       # Custom exceptions handling
│   │   │       ├── models/                           # Entity models including (Player, Game, Deck, Card)
│   │   │       ├── repositories/                     # Reactive repositories for MongoDB and MySQL
│   │   │       ├── services/                         # Business logic services
│   │   │       └── S05T01N01Application.java         # Main Spring Boot application entry point
│   │   └── resources/
│   │       ├── application.yml                       # Application configuration (DB, ports, etc.)
│   │       └── init.sql                              # MySQL database initialization script
│   └── test/
│       └── java/
│           └── cat/itacademy/s05/t01/n01/S05T01N01/
│               ├── ExceptionTestController.java                     # Tests for exception handling controller
│               ├── GameControllerIntegrationTest.java               # Integration tests for GameController
│               ├── GameControllerTest.java                          # Unit tests for GameController
│               ├── GameServiceTest.java                             # Unit tests for GameService logic
│               ├── GlobalExceptionHandlerIntegrationTest.java       # Integration tests for global exception handling
│               └── PlayerControllerIntegrationTest.java             # Integration tests for PlayerController
├── docker-compose.yml                           # Docker Compose for MongoDB and MySQL containers
├── pom.xml                                      # Maven project file (dependencies & build plugins)
└── README.md                                    # Project documentation and instructions


 </pre>
## 🚀 Getting Started

### 1️⃣ Clone the repo

```bash
git clone https://github.com/AdriaVH/5.1-Spring_Advanced-Blackjack-N1.git
cd 5.1-Spring_Advanced-Blackjack-N
```
### 2️⃣ Start the databases (MySQL + MongoDB)
```bash
docker-compose up -d
```
### 3️⃣ Run the application
```bash
./mvnw spring-boot:run
```
## 🔌 API Endpoints

### 🎮 `/games`

| Method | Endpoint           | Description                 |
|--------|--------------------|-----------------------------|
| POST   | `/games`           | Create new game             |
| GET    | `/games/{id}`      | Get game details            |
| POST   | `/games/{id}/play` | Make a move (HIT / STAND)   |
| DELETE | `/games/{id}`      | Delete game                 |

### 👤 `/player`

| Method | Endpoint         | Description                  |
|--------|------------------|------------------------------|
| POST   | `/player`        | Create new player            |
| PUT    | `/player/{id}`   | Update player name           |
| GET    | `/ranking`       | Get win-rate ranking of players |


### 📚 Swagger UI

After running the app, access API docs here:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---
---

### ✅ Testing

Tests are written using:

- **WebTestClient** for integration tests  
- **Mockito** for mocking  
- **StepVerifier** for Reactor stream assertions  

Example test flow:

1. Create player  
2. Create game  
3. Play moves  
4. Verify responses and stats

---

### 🧠 Notes

- Player balance uses **`double`** type with null-safe helpers  
- **MongoDB** stores the dynamic, nested game state  
- **MySQL** stores static player info via **R2DBC** for non-blocking SQL  

---

### 🐳 Docker Compose Environment Overview

The app uses these services with these environment variables and ports:

- **MongoDB**  
  - Username: `root`  
  - Password: `root`  
  - Port: `27017`  

- **Mongo Express** (MongoDB web UI)  
  - Username: `root`  
  - Password: `root`  
  - Port: `8081`  

- **MySQL**  
  - Root password: `root`  
  - Database: `blackjack`  
  - User password: `root`  
  - Port mapping: `3307` (host) → `3306` (container)

---

### 👨‍💻 Author

Adrià VH  
🔗 [GitHub](https://github.com/AdriaVH/5.1-Spring_Advanced-Blackjack-N1)
