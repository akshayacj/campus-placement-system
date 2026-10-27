# Campus Placement & Recruitment Portal Backend (TTT-JFS-01)

Spring Boot 3 backend foundation designed both as an enterprise microservice foundation and as an interactive, live educational codebase for the tech talk:
> **"Inside Spring Boot: Auto-configuration, Starters and the Application Context"**

---

## 1. Prerequisites

- **Java Development Kit (JDK):** Java 21 LTS
- **Build Tool:** Apache Maven 3.9+
- **Container Runtime:** Docker & Docker Compose (or MySQL 8.0 local server)

---

## 2. Quickstart Instructions

### Step 1: Start MySQL 8.0 Container
From the repository root:
```bash
docker compose up -d mysql
```
Verify container is healthy:
```bash
docker compose ps
```

### Step 2: Run Application with Development Profile
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Flyway automatically applies `V1__init.sql` (schema) and `V2__seed_branches.sql` (CSE, ECE, EEE, MECH, CIVIL, IT) on startup.

---

## 3. Interactive Tech Talk Endpoints

| Endpoint | Profile / Parameters | Description |
| :--- | :--- | :--- |
| `GET /api/v1/ping` | Any | Verifies Spring MVC & DispatcherServlet status: `{"status":"ok"}`. |
| `GET /api/v1/branches` | `dev` or `test` | Returns 6 academic branches seeded by Flyway via BranchController -> BranchService -> BranchRepository. |
| `GET /api/v1/demo/di/constructor` | Any | Shows constructor injection with final fields and Mockito-testable design. |
| `GET /api/v1/demo/di/field` | Any | Shows field injection with in-depth comments explaining why it is discouraged. |
| `GET /api/v1/demo/scopes` | Any | Visualizes Singleton (identity hash & counter increments), Prototype (fresh instance via `ObjectProvider`), and Request scope (distinct UUID per HTTP request). |
| `GET /api/v1/demo/beans` | Any | Live reflection of all beans registered in `com.college.placement` along with their scopes. |
| `GET /api/v1/demo/config` | Any | Displays bound `@ConfigurationProperties` (`PlacementProperties`) with masked JWT secret. |
| `GET /api/v1/demo/storage` | `dev` | Returns `LocalDiskResumeStorageService` (auto-configured default). |
| `GET /api/v1/demo/storage` | `dev,test-storage` | Returns `InMemoryResumeStorageService` (proves `@ConditionalOnMissingBean` back-off). |
| `GET /api/v1/demo/datasource` | `dev` | Returns auto-configured Hikari pool (`HikariPool-1`, pool size 10). |
| `GET /api/v1/demo/datasource` | `dev,custom-datasource` | Returns custom overridden pool (`PLACEMENT-CUSTOM-POOL`, pool size 5). |
| `GET /actuator/health` | `dev` | Detailed health indicator output including custom `ResumeStorageHealthIndicator`. |
| `GET /actuator/conditions` | `dev` | Live HTTP equivalent of `--debug` ConditionEvaluationReport. |

### Swagger UI / OpenAPI Documentation
Interactive Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

---

## 4. Running with Debug Auto-Configuration Report

To print the full `CONDITIONS EVALUATION REPORT` to your console during startup:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--debug
```
Or view the summarized count without scrolling:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev,report
```

---

## 5. Fail-Fast Configuration Demo

`PlacementProperties` enforces strict validation rules via `@Validated` and Jakarta Validation constraints (`@Positive`, `@Min`, `@NotBlank`).

To demonstrate fail-fast validation at startup, pass an invalid value for `offerResponseWindowHours`:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--placement.policy.offer-response-window-hours=-1"
```

### Expected Startup Failure Output:
```
***************************
APPLICATION FAILED TO START
***************************

Description:
Binding to target org.springframework.boot.context.properties.bind.BindResult@... failed:

    Property: placement.policy.offer-response-window-hours
    Value: -1
    Reason: must be greater than or equal to 1

Action:
Update your application's configuration
```
The application halts immediately during context creation, preventing invalid state from entering the system.

---

## 6. Running Unit & Integration Tests

The test suite is fully decoupled from external infrastructure. Running:
```bash
mvn clean verify
```
runs all unit tests, Mockito tests, `ApplicationContextRunner` conditional tests, and in-memory H2 repository tests without requiring MySQL to be running.

