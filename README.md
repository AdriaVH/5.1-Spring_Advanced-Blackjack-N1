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

## 🗂️ Project Structure

src
├── controllers/ # REST Controllers
├── DTOs/ # Request/Response DTOs + mappers
├── exceptions/ # Custom exception classes
├── models/ # Entities (Mongo & MySQL)
├── repositories/ # R2DBC and Mongo repositories
├── services/ # Business logic and helpers
└── tests/ # Unit and integration tests

---

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
