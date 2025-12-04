# Spring Boot Blog API

A simple RESTful Blog API built with Spring Boot. It manages Posts, Users, and Comments with validation, DTOs, and Liquibase database migrations.

## Tech Stack
- Java 17+
- Spring Boot (Web, Validation)
- Liquibase (database migrations)
- Maven (build)
- JUnit 5, Spring Test, Mockito (tests)

## Project Structure
- `src/main/java/com/example/blog_api/`
  - `controller/` — REST controllers (`PostController`, `UserController`, `CommentController`)
  - `service/` — service interfaces
  - `service/impl/` — service implementations
  - `dto/` — request/response DTOs
  - `entity/` — JPA entities
  - `exception/` — exception types and handlers
  - `repository/` — Spring Data repositories
- `src/main/resources/`
  - `application.yaml` — app configuration (profiles, DB)
  - `db/changelog/` — Liquibase changelogs
- `Blog API.postman_collection.json` — Postman collection ready to import

## Configuration
Set your database connection and any app settings in `src/main/resources/application.yaml`.

Environment overrides (optional): you can use `application.env` or system environment variables. For local dev with H2 or Postgres, configure a datasource and Liquibase properties in `application.yaml`.

## Build & Run
Using the Maven wrapper on Windows PowerShell:

```powershell
# Build the project
C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\mvnw.cmd -f C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\pom.xml clean package

# Run the application
java -jar C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\target\blog-api-0.0.1-SNAPSHOT.jar
```

By default, the API runs at `http://localhost:8080`.

## Database & Migrations (Liquibase)
Liquibase changelogs are under `src/main/resources/db/changelog/`. On application startup, Liquibase applies pending migrations to the configured database.

- To change DB settings, edit `application.yaml`.
- For first run, ensure the database exists and credentials are valid.

## API Endpoints
Base URL: `http://localhost:8080`

- Posts
  - `GET /api/posts` — list posts
  - `GET /api/posts/{id}` — get post by id
  - `POST /api/posts` — create post
  - `PUT /api/posts/{id}` — update post
  - `DELETE /api/posts/{id}` — delete post
- Users
  - `GET /api/users` — list users
  - `GET /api/users/{id}` — get user by id
  - `POST /api/users` — create user
  - `PUT /api/users/{id}` — update user
  - `DELETE /api/users/{id}` — delete user
- Comments
  - `POST /api/comments` — create comment
  - `GET /api/comments/{id}` — get comment by id
  - `GET /api/posts/{postId}/comments` — list comments for a post
  - `DELETE /api/comments/{id}` — delete comment

## Postman Collection
Import `Blog API.postman_collection.json` into Postman:
- Set environment variable `base_url` to `http://localhost:8080`.
- Requests are grouped under Posts, Users, and Comments.
- The collection avoids inline scripts and uses Postman dynamic variables (e.g., `{{$timestamp}}`) where useful.

## Testing
Unit and controller tests are included.

Run all tests:
```powershell
C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\mvnw.cmd -f C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\pom.xml test
```

Run a specific test (example: `UserServiceTest`):
```powershell
C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\mvnw.cmd -f C:\Users\zen\Desktop\my-space\learn-java\roadmap\projects\blog-api\pom.xml -Dtest=UserServiceTest test
```

Notes on DB impact during tests:
- Service tests use Mockito to mock repositories; they do not hit a real database.
- Controller tests mock the service layer; they also do not affect the database.
- If you later add integration tests with a real DataSource, prefer an in-memory DB (H2) or Testcontainers to avoid affecting dev/prod data.

## Troubleshooting
- If the app fails to start due to DB connection issues, verify `application.yaml` datasource settings.
- Liquibase errors typically indicate a changelog or schema mismatch; check `src/main/resources/db/changelog` and the target DB state.
- For port conflicts, change `server.port` in `application.yaml`.

## License
This project is licensed under the terms in `LICENSE`.

