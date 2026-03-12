# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BYU CS 240 Chess - A multiplayer chess server and command line chess client demonstrating client/server architecture, HTTP/WebSocket networking, MySQL database persistence, and proper software design.

## Build & Test Commands

This is a multi-module Maven project with three modules: `shared`, `server`, and `client`.

### Building & Running
```bash
# Build all modules
mvn compile

# Build without tests (creates uber jars)
mvn package -DskipTests

# Run server (starts on port 8080)
mvn -pl server exec:java

# Run client
mvn -pl client exec:java

# Run from uber jar
java -jar server/target/server-jar-with-dependencies.jar
java -jar client/target/client-jar-with-dependencies.jar
```

**Windows PowerShell:**
```powershell
# Build all modules
mvn compile

# Build without tests (creates uber jars)
mvn package "-DskipTests"

# Run server (starts on port 8080)
mvn -pl server exec:java

# Run client
mvn -pl client exec:java

# Run from uber jar
java -jar server\target\server-jar-with-dependencies.jar
java -jar client\target\client-jar-with-dependencies.jar
```

### Testing
```bash
# Run all tests
mvn test

# Run tests for specific module
mvn -pl shared test
mvn -pl server test
mvn -pl client test

# Run specific test class
mvn test -Dtest=StandardAPITests
mvn test -Dtest=DatabaseTests
```

**Windows PowerShell:**
```powershell
# Run all tests
mvn test

# Run tests for specific module
mvn -pl shared test
mvn -pl server test
mvn -pl client test

# Run specific test class
mvn test "-Dtest=StandardAPITests"
mvn test "-Dtest=DatabaseTests"
```

## Architecture

### Three-Module Structure

1. **Shared** - Chess game logic and data models used by both client and server
   - Chess rules implementation (ChessGame, ChessBoard, ChessPiece, ChessMove)
   - Data transfer objects (GameData, UserData, AuthData)

2. **Server** - HTTP REST API using Javalin framework
   - Entry point: `ServerMain.java` (starts server on port 8080)
   - Server setup: `Server.java`

3. **Client** - Command line interface
   - Entry point: `ClientMain.java`

### Server Architecture

**Three-Layer Pattern:**

1. **HTTP Layer** (`server/Server.java`)
   - Javalin-based REST endpoints
   - Route handlers that delegate to services
   - Exception handling for ServiceException
   - Custom Gson JSON mapper

2. **Service Layer** (`service/`)
   - `UserService` - Registration, login, logout with BCrypt password hashing
   - `GameService` - Create, list, join games with authorization checks
   - `ClearService` - Database clearing for testing
   - Services throw `ServiceException` with HTTP status codes
   - All services validate auth tokens before operations

3. **Data Access Layer** (`dataaccess/`)
   - DAO interfaces: `UserDAOInterface`, `GameDAOInterface`, `AuthDAOInterface`
   - SQL implementations: `SQLUserDAO`, `SQLGameDAO`, `SQLAuthDAO`
   - In-memory implementations: `UserDAO`, `GameDAO`, `AuthDAO`
   - `DatabaseManager` - MySQL connection management and schema creation
   - Database credentials in `server/src/main/resources/db.properties`

**Key Design Patterns:**
- Dependency injection: Server instantiates DAOs and passes them to Services
- Interface-based DAOs allow swapping between SQL and in-memory implementations
- Services handle business logic and authorization
- DAOs handle only data persistence

### Database Schema

Three tables (auto-created on server start):
- `users` - username (PK), email, hashedPassword (BCrypt)
- `games` - gameID (auto-increment PK), whiteUsername, blackUsername, gameName, chessGame (serialized JSON)
- `auth` - username, authToken

Connection details in `server/src/main/resources/db.properties` (default: localhost:3306, database: chess, user: root, password: password)

## REST API Endpoints

- `DELETE /db` - Clear all data (testing only)
- `POST /user` - Register new user (body: UserData)
- `POST /session` - Login (body: UserData with username/password)
- `DELETE /session` - Logout (header: authorization)
- `GET /game` - List games (header: authorization)
- `POST /game` - Create game (header: authorization, body: {gameName})
- `PUT /game` - Join game (header: authorization, body: {gameID, playerColor})

All authenticated endpoints require `authorization` header with auth token.

## Dependencies

- Java 21
- Javalin 6.4.0 - HTTP server framework
- MySQL Connector 9.4.0 - Database driver
- Gson 2.10.1 - JSON serialization
- BCrypt 0.4 (jbcrypt) - Password hashing
- JUnit Jupiter 5.9.2 - Testing

## Development Notes

- The server automatically creates the database and tables on startup via `DatabaseManager.createDatabase()` and `DatabaseManager.createTables()`
- When working with DAOs, remember there are both SQL and in-memory implementations - check which is instantiated in `Server.java`
- Password storage: User passwords are hashed with BCrypt before storage in `SQLUserDAO.createUser()` and verified in `UserService.login()`
- GameData includes a serialized ChessGame object stored as JSON text in the database
- Auth tokens are generated as UUIDs in the DAO layer
- The project uses phased development with starter code in `starter-code/` directory (phases 1-6)
