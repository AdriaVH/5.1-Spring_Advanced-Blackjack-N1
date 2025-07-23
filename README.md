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

yaml
Copiar
Editar

---

## 🚀 Getting Started

### 1️⃣ Clone the repo

```bash
git clone https://github.com/AdriaVH/5.1-Spring_Advanced-Blackjack-N1.git
cd 5.1-Spring_Advanced-Blackjack-N1
2️⃣ Start the databases (MySQL + MongoDB)
bash
Copiar
Editar
docker-compose up -d
3️⃣ Run the application
bash
Copiar
Editar
./mvnw spring-boot:run

🔌 API Endpoints
🎮 /games
Method	Endpoint	Description
POST	/games	Create new game
GET	/games/{id}	Get game details
POST	/games/{id}/play	Make a move (HIT / STAND)
DELETE	/games/{id}	Delete game

👤 /player
Method	Endpoint	Description
POST	/player	Create new player
PUT	/player/{id}	Update player name
GET	/ranking	Get win-rate ranking of players

📚 Swagger UI
After running the app, access API docs:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

🐳 Docker Compose
yaml
Copiar
Editar
version: '3.8'

✅ Testing
Tests are written using:

WebTestClient for integration tests

Mockito for mocking

StepVerifier for Reactor stream assertions

Example test flow:

Create player

Create game

Play moves

Verify responses and stats

🧠 Notes
double is used for player balance (use null-safe helpers).

MongoDB is used for game state due to dynamic and nested nature of gameplay.

MySQL stores static player info with R2DBC (non-blocking SQL).

👨‍💻 Author
Adrià VH
🔗 GitHub
