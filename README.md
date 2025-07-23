# 🃏 Blackjack WebFlux API

A reactive Blackjack game API using Spring WebFlux, R2DBC with MySQL, MongoDB for game state, and Swagger/OpenAPI for documentation. Supports player registration, game creation, move execution, and player rankings.

---

## 📦 Tech Stack

- **Java 21** (from `<java.version>21</java.version>`)
- **Spring Boot 3.2.4** (parent version)
- **Spring WebFlux** (`spring-boot-starter-webflux`)
- **R2DBC MySQL 1.0.2** (`io.asyncer:r2dbc-mysql:1.0.2`) for reactive MySQL player persistence
- **MongoDB Reactive** (`spring-boot-starter-data-mongodb-reactive`) for game state storage
- **Maven** (project & build management)
- **Lombok 1.18.30** for boilerplate code reduction
- **OpenAPI/Swagger 2.3.0** (`springdoc-openapi-starter-webflux-ui`) for API documentation
- **JUnit 5**, **Mockito 5.5.0**, **Reactor Test** for testing


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
│   │   │       ├── models/                           # Entity models (Player, Game, Deck, Card)
│   │   │       ├── repositories/                     # Reactive repositories for MongoDB and MySQL
│   │   │       ├── services/                         # Business logic services
│   │   │       ├── utils/                            # Utility classes and helpers
│   │   │       └── S05T01N01Application.java         # Main Spring Boot application entry point
│   │   └── resources/
│   │       └── ...                                   # Application internal resources (if any)
│   └── test/
│       └── java/
│           └── cat/itacademy/s05/t01/n01/S05T01N01/
│               ├── ExceptionTestController.java
│               ├── GameControllerIntegrationTest.java
│               ├── GameControllerTest.java
│               ├── GameServiceTest.java
│               ├── GlobalExceptionHandlerIntegrationTest.java
│               └── PlayerControllerIntegrationTest.java
│
├── docker-compose.yml                               # Docker Compose for MongoDB and MySQL containers
├── application.yml                                  # Application configuration used by Docker Compose and app
├── init.sql                                         # MySQL DB initialization script to create the DDBB and tables
├── pom.xml                                          # Maven project file (dependencies & build plugins)
└── README.md                                        # Project documentation and instructions

 </pre>
## 🚀 Getting Started

### 1️⃣ Clone the repo

```bash
git clone https://github.com/AdriaVH/5.1-Spring_Advanced-Blackjack-N1.git
cd 5.1-Spring_Advanced-Blackjack-N1
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

| Method | Endpoint           | Description               |
|--------|--------------------|---------------------------|
| POST   | `/games`           | Create new game           |
| GET    | `/games/{id}`      | Get game details          |
| POST   | `/games/{id}/play` | Make a move (HIT / STAND) |
| DELETE | `/games/{id}`      | Delete game               |

### 👤 `/player`

| Method | Endpoint         | Description            |
|--------|------------------|------------------------|
| POST   | `/player`        | Create new player      |
| PUT    | `/player/{id}`   | Update player name     |

### 📊 `/ranking`

| Method | Endpoint    | Description                                   |
|--------|-------------|-----------------------------------------------|
| GET    | `/ranking`  | Get players' ranking and their win rate       |



### 📚 Swagger UI

After running the app, access API docs here:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---
---
# TASK 5.1 API Blackjack amb Spring Boot Reactiu

## 📖 Descripció

En aquest exercici pràctic, crearem una **API en Java amb Spring Boot** per a un joc de Blackjack.  
L'API gestionarà informació en dues bases de dades diferents: **MongoDB** i **MySQL**.  
El joc inclourà totes les funcionalitats bàsiques per jugar: gestió de jugadors, mans de cartes i regles del joc.

La aplicació estarà ben documentada i testada amb eines com README.md i Swagger.

---

## 🚦 Nivell 1: Implementació Bàsica

- Desenvolupament d'una aplicació **reactiva** amb Spring WebFlux.  
- Configuració de **MongoDB reactiva** i implementació de controladors i serveis reactius.  
- Gestió global d'excepcions amb un `GlobalExceptionHandler`.  
- Configuració per utilitzar dos esquemes de bases de dades: **MySQL** i **MongoDB**.  
- Proves unitàries per almenys un controlador i un servei amb **JUnit** i **Mockito**.  
- Documentació automàtica de l’API amb **Swagger**.

---

## 🛠️ Passos a seguir

1. **Disseny de l'API:**  
   Defineix els endpoints per gestionar el joc: crear partida, jugar, obtenir detalls, etc.

2. **Connexió a les bases de dades:**  
   Configura la connexió a MongoDB i MySQL.  
   Crea les entitats Java per representar les dades del joc.

3. **Proves unitàries:**  
   Escriu proves amb JUnit i Mockito per verificar la correcta funcionalitat de l’API i la persistència.

---

## 🎯 Endpoints per al joc

### Crear partida  
- **Mètode:** `POST`  
- **Endpoint:** `/game/new`  
- **Cos de la sol·licitud:** Nom del jugador nou  
- **Resposta exitosa:** `201 Created` amb info de la partida creada

### Obtenir detalls de partida  
- **Mètode:** `GET`  
- **Endpoint:** `/game/{id}`  
- **Paràmetres:** Identificador de la partida (`id`)  
- **Resposta exitosa:** `200 OK` amb els detalls de la partida

### Realitzar jugada  
- **Mètode:** `POST`  
- **Endpoint:** `/game/{id}/play`  
- **Paràmetres:** Identificador de la partida (`id`)  
- **Cos de la sol·licitud:** Tipus de jugada i quantitat apostada  
- **Resposta exitosa:** `200 OK` amb resultat de la jugada i estat actual

### Eliminar partida  
- **Mètode:** `DELETE`  
- **Endpoint:** `/game/{id}/delete`  
- **Paràmetres:** Identificador de la partida (`id`)  
- **Resposta exitosa:** `204 No Content` si s'elimina correctament

### Obtenir rànquing de jugadors  
- **Mètode:** `GET`  
- **Endpoint:** `/ranking`  
- **Resposta exitosa:** `200 OK` amb llista ordenada per posició i puntuació

### Canviar nom del jugador  
- **Mètode:** `PUT`  
- **Endpoint:** `/player/{playerId}`  
- **Paràmetres:** Identificador del jugador (`playerId`)  
- **Cos de la sol·licitud:** Nou nom del jugador  
- **Resposta exitosa:** `200 OK` amb informació actualitzada del jugador

---

💡 **Consell:**  
Per als endpoints que requereixin cos de la sol·licitud, assegura't d'enviar JSON en format correcte per a la facilitat d'ús i integració.

---

Espero que aquesta versió t’agradi! Vols que t'ajudi també a afegir exemples de cossos JSON per a cada endpoint?


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
