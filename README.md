# Login Risk Monitor

A security-focused server-side rendered (SSR) Spring Boot application that records authentication attempts, assigns risk levels and provides role-based login monitoring.

Developed as a final project for Coding Factory 9 at the Athens University of Economics and Business (AUEB).

## Live Demo

Application: https://login-risk-monitor.onrender.com

> Note: The live demo uses free-tier hosting. The first request after a period of inactivity may take up to a minute while the service starts.

## Stack

- Language: Java 21
- Frameworks / Libraries:
    - Spring Boot 3
    - Spring Security
    - Spring Data JPA / Hibernate
    - Thymeleaf
    - Bean Validation
    - Flyway
    - Lombok
- Database: MySQL 8
- Testing:
    - JUnit 5
    - Mockito
    - H2
- Build Tool: Maven
- Deployment:
    - Docker
    - Docker Compose

## Features

- Records successful and failed login attempts.
- Stores submitted username, IP address and timestamp.
- Classifies login attempts as `LOW`, `MEDIUM` or `HIGH` risk.
- Provides overall login statistics for administrators.
- Provides personal login statistics and history for regular users.
- Supports `ADMIN` and `USER` role-based authorization.
- Allows administrators to view users and create new accounts.
- Stores passwords using BCrypt.
- Uses Flyway for versioned database migrations.
- Uses DTOs and mappers between the domain and view layers.

## Entry Point

Main class:

```text
com.loginriskmonitor.LoginRiskMonitorApplication
```

The default Spring profile is:

```text
dev
```

## Architecture

The application uses server-side rendering with Thymeleaf and follows a layered architecture:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
MySQL

Service -> DTO / Mapper -> View
```

Main package structure:

```text
src/main/java/com/loginriskmonitor
├── controller
├── domain
├── dto
├── exception
├── mapper
├── repository
├── security
└── service
```

Spring MVC handles page flow, Thymeleaf renders the frontend and Spring Security handles authentication and authorization.

## Domain Model

The main domain objects are:

- `User`
- `Role`
- `LoginAttempt`
- `RiskLevel`

Database relationships and login-attempt data:

```text
ROLE
├── id (PK)
└── name

USER
├── id (PK)
├── username
├── password
└── role_id (FK -> ROLE.id)

LOGIN_ATTEMPT
├── id (PK)
├── username
├── ip_address
├── successful
├── risk_level
└── attempted_at (TIMESTAMP)

ROLE 1 -------- * USER
```

`LoginAttempt` stores the submitted username instead of requiring a direct relationship with `User`.

This allows failed login attempts to be recorded even when the submitted username does not exist.

## Risk Logic

Login attempts are classified as follows:

```text
Successful login              -> LOW
First or second failed login  -> MEDIUM
Third and later failed login  -> HIGH
```

Failed attempts are calculated using the stored login history for the submitted username.

## Timestamp Handling

Login attempt timestamps are stored using `Instant` and persisted in MySQL using `TIMESTAMP`.

The application stores the actual point in time independently of the server timezone and converts timestamps to:

```text
Europe/Athens
```

when preparing login-attempt data for display.

## Configuration and Profiles

The application uses Spring profiles for different environments.

Available profiles:

- `dev` — local development
- `prod` — production deployment
- `test` — automated tests

### Development

The development profile connects to:

```text
localhost:3306/login_risk_monitor
```

Required environment variable:

```text
DB_PASSWORD
```

Optional database username:

```text
DB_USERNAME
```

If `DB_USERNAME` is not provided, the development configuration uses `root`.

### Production

Production database configuration is provided through environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Activate the production profile with:

```text
SPRING_PROFILES_ACTIVE=prod
```

Database credentials are not stored in the repository.

### Test

The test profile uses an in-memory H2 database.

Flyway is disabled during tests and Hibernate creates and removes the test schema automatically.

## Running Locally

### Requirements

- JDK 21
- MySQL 8

Set the local MySQL password before starting the application.

### Windows PowerShell

```powershell
$env:DB_PASSWORD="your_mysql_password"
.\mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
export DB_PASSWORD="your_mysql_password"
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

## Running with Docker

Docker Compose starts both the Spring Boot application and MySQL.

Create a `.env` file in the project root:

```env
DB_USERNAME=loginrisk
DB_PASSWORD=your_database_password
MYSQL_ROOT_PASSWORD=your_root_password
```

The `.env` file is excluded from Git through `.gitignore`.

Start the application:

```bash
docker compose up --build
```

Open:

```text
http://localhost:8080
```

Stop the containers:

```bash
docker compose down
```

To also remove the local MySQL volume:

```bash
docker compose down -v
```

## Demo Accounts

Local demo accounts are created through Flyway migrations.

| Role | Username | Password |
| --- | --- | --- |
| ADMIN | `admin` | `Admin123!` |
| USER | `user` | `User123!` |

These credentials are intended for local demonstration only and should be changed or removed in a real production environment.

Administrators can also create new `ADMIN` or `USER` accounts through the application.

New passwords are stored using BCrypt.

## Database Migrations

Flyway migration scripts are located under:

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__create_initial_schema.sql
V2__insert_initial_data.sql
V3__insert_test_user.sql
V4__insert_regular_user.sql
V5__change_attempted_at_to_timestamp.sql
```

Flyway automatically validates and applies pending migrations when the application starts.

Hibernate uses:

```text
spring.jpa.hibernate.ddl-auto=validate
```

so the database schema is managed by Flyway rather than generated automatically in development or production.

## Testing

Tests are located under:

```text
src/test/java
```

The project includes:

- Login risk calculation tests.
- Login-attempt normalization tests.
- Dashboard statistics tests.
- User service tests.
- Role service tests.
- Application-context test.

Run all tests on Windows:

```powershell
.\mvnw.cmd clean test
```

Run all tests on macOS / Linux:

```bash
./mvnw clean test
```

MySQL is not required for the automated tests because the test profile uses H2.

## Building

Build the executable JAR on Windows:

```powershell
.\mvnw.cmd clean package
```

On macOS / Linux:

```bash
./mvnw clean package
```

The generated JAR is stored under:

```text
target/
```

## Deployment

The application can be deployed using the included Dockerfile.

The current live deployment uses:

```text
GitHub
   |
   v
Render
   |
   v
Aiven MySQL
```

Production configuration is supplied through environment variables rather than committed credentials.

Flyway runs database migrations during application startup and Hibernate validates the resulting schema.

## Security

- Authentication is implemented with Spring Security.
- Authorization is based on `ADMIN` and `USER` roles.
- Administrator URLs are protected server-side.
- Regular users can access only their own statistics and login history.
- Passwords are stored using BCrypt.
- CSRF protection remains enabled.
- Authentication success and failure handlers record login attempts.
- Secrets and local `.env` files are excluded from Git.

## Known Limitations

- Risk calculation uses the complete stored login history rather than a rolling time window.
- Successful authentication does not reset the historical failed-attempt count.
- Login-attempt lists are not currently paginated.
- Rate limiting and account lockout are not implemented.
- The application uses `Europe/Athens` as the display timezone.
- When running behind a reverse proxy, additional forwarded-header configuration may be required to capture the original client IP address.