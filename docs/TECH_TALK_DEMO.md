# Tech Talk Guide: Inside Spring Boot

**Title:** Inside Spring Boot: Auto-configuration, Starters and the Application Context  
**Target Codebase:** Campus Placement & Recruitment Portal (`placement-backend`)  
**Base Package:** `com.college.placement`

---

## 1. Slide-by-Slide Outline & Live Code Mapping

| Key Point | File to Open | Endpoint / Command | What to Say to Audience |
| :--- | :--- | :--- | :--- |
| **1. The Starter Illusion & Auto-Configuration** | `pom.xml`, `report-1-bare.txt` vs `report-2-jpa.txt`, `DIFF-NOTES.md` | `mvn spring-boot:run -Dspring-boot.run.arguments=--debug` | "Starters are not libraries; they are curated sets of transitive dependencies. The moment `spring-boot-starter-data-jpa` lands on the classpath, Spring Boot's condition engine detects Hibernate, Hikari, and JPA interfaces, flipping them from Negative to Positive matches without a single line of boilerplate." |
| **2. Anatomy of `@SpringBootApplication`** | `PlacementApplication.java`, `OutsideScanConfig.java`, `ComponentScanProofBean.java` | Test: `ComponentScanTest` | "@SpringBootApplication is an aggregate of `@SpringBootConfiguration`, `@EnableAutoConfiguration`, and `@ComponentScan`. By default, it only scans downward from its own package. Show `com.college.outside`: it remains invisible until profile `scan-demo` explicitly imports it." |
| **3. Dependency Injection: Constructor vs Field** | `GreetingService.java`, `ConstructorInjectedController.java`, `FieldInjectedController.java` | `GET /api/v1/demo/di/constructor`<br>`GET /api/v1/demo/di/field` | "Field injection `@Autowired` is an anti-pattern: breaks immutability, hides dependencies, and makes plain Java unit testing impossible without Spring. See `ConstructorInjectionTest`: constructor injection tests in 1 millisecond using plain `new` and Mockito." |
| **4. Stereotypes, Lifecycles & Bean Scopes** | `LifecycleDemoBean.java`, `ScopesDemoController.java`, `BeansDemoController.java` | `GET /api/v1/demo/scopes`<br>`GET /api/v1/demo/beans`<br>`GET /api/v1/demo/stereotypes` | "All stereotypes (@Service, @Repository, @Controller) are specialized @Components. Watch console logs during startup: `[LIFECYCLE 1/5]` through `5/5`. Hit `/demo/scopes` twice: singleton maintains state and identityHashCode; prototype creates a new instance every time via `ObjectProvider`; request-scoped creates a UUID per HTTP request. Explain the 'prototype in singleton' trap." |
| **5. Custom Auto-Configuration & `@Conditional`** | `ResumeStorageAutoConfiguration.java`, `LocalDiskResumeStorageService.java`, `InMemoryResumeStorageService.java` | `GET /api/v1/demo/storage`<br>Actuator: `/actuator/conditions` | "Here is how Spring Boot's own authors write starters. Our `ResumeStorageAutoConfiguration` uses `@ConditionalOnMissingBean`. By default, you get local disk storage. Activate profile `test-storage`, and Spring automatically backs off in favor of our in-memory bean." |
| **6. Configuration Precedence & DataSource Override** | `PlacementProperties.java`, `CustomDataSourceConfig.java`, `application-dev.yml` | `GET /api/v1/demo/config`<br>`GET /api/v1/demo/datasource` | "Spring Boot's externalized configuration is evaluated across 17+ property sources. Immutable Java 21 records with `@ConfigurationProperties` guarantee type safety and fail-fast validation. By creating a custom `DataSource` bean with `@Profile("custom-datasource")`, Spring's auto-configured Hikari pool yields immediately." |

---

## 2. Step-by-Step Live Demo Script

### Phase 1: Bare Starter (demo-1-bare)
```bash
# 1. Switch to bare starter tag
git checkout demo-1-bare

# 2. Inspect minimal dependencies
cat backend/pom.xml

# 3. View the autoconfig debug report
# Notice DataSourceAutoConfiguration and HibernateJpaAutoConfiguration in 'Negative matches'
less docs/autoconfig-reports/report-1-bare.txt
```

### Phase 2: JPA Starter Transition (demo-2-jpa)
```bash
# 1. Switch to JPA tag
git checkout demo-2-jpa

# 2. Inspect what was added: spring-boot-starter-data-jpa, mysql-connector-j, flyway-core
git diff demo-1-bare backend/pom.xml

# 3. View the diff notes showing shifts from Negative to Positive matches
cat docs/autoconfig-reports/DIFF-NOTES.md

# 4. Notice DataSource, Flyway, and Hibernate are now positive matches
less docs/autoconfig-reports/report-2-jpa.txt
```

### Phase 3: Live DataSource Override (demo-3-datasource-override)
```bash
# 1. Switch to DataSource override tag
git checkout demo-3-datasource-override

# 2. Start MySQL container
docker compose up -d mysql

# 3. Run application with default dev profile
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 4. In another terminal, inspect default Hikari pool:
curl -s http://localhost:8080/api/v1/demo/datasource | jq .
# Expected output:
# {
#   "dataSourceClass": "com.zaxxer.hikari.HikariDataSource",
#   "poolName": "HikariPool-1",
#   "maximumPoolSize": 10
# }

# 5. Stop the app and restart with custom-datasource profile:
mvn spring-boot:run -Dspring-boot.run.profiles=dev,custom-datasource

# 6. Re-check the endpoint:
curl -s http://localhost:8080/api/v1/demo/datasource | jq .
# Expected output:
# {
#   "dataSourceClass": "com.zaxxer.hikari.HikariDataSource",
#   "poolName": "PLACEMENT-CUSTOM-POOL",
#   "maximumPoolSize": 5
# }

# 7. Check /actuator/conditions to see DataSourceConfiguration.Hikari back off:
curl -s http://localhost:8080/actuator/conditions | jq '.contexts.application.negativeMatches."DataSourceConfiguration.Hikari"'
```

### Phase 4: Full Foundation Tour (master branch)
```bash
git checkout master

# Run test suite without needing MySQL
mvn clean verify

# Start app with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
- Open Swagger UI: `http://localhost:8080/swagger-ui.html`
- Call Scopes twice: `curl http://localhost:8080/api/v1/demo/scopes`
- Call Beans listing: `curl http://localhost:8080/api/v1/demo/beans`
- Call Branches (Flyway seeded): `curl http://localhost:8080/api/v1/branches`
- Switch Storage bean: `mvn spring-boot:run -Dspring-boot.run.profiles=dev,test-storage` and check `curl http://localhost:8080/api/v1/demo/storage`

---

## 3. Configuration Precedence Demonstration

Spring Boot resolves configuration properties using a well-defined order of precedence.
Highest precedence overrides lowest:

$$\text{Command Line Arguments} > \text{OS Environment Variables} > \text{Profile YAML (e.g. application-dev.yml)} > \text{Base YAML (application.yml)}$$

### Live Commands to Prove Precedence:

1. **Base YAML default (`application.yml`):**
   `offer-response-window-hours: 72`
2. **Override via Profile YAML (`application-dev.yml` or inline):**
   If specified in `application-dev.yml`, it overrides `application.yml`.
3. **Override via OS Environment Variable (Relaxed Binding):**
   ```bash
   export PLACEMENT_POLICY_OFFERRESPONSEWINDOWHOURS=48
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   curl -s http://localhost:8080/api/v1/demo/config | jq .properties.policy.offerResponseWindowHours
   # Returns: 48
   ```
4. **Override via Command Line Argument (Wins over Environment Variable):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--placement.policy.offer-response-window-hours=24"
   curl -s http://localhost:8080/api/v1/demo/config | jq .properties.policy.offerResponseWindowHours
   # Returns: 24 (CLI argument overrides OS environment variable 48 and YAML 72)
   ```

---

## 4. Troubleshooting: "What Can Go Wrong" During Live Demos

| Issue | Root Cause | Immediate Fix |
| :--- | :--- | :--- |
| **MySQL Connection Refused** (`CommunicationsException: Communications link failure`) | MySQL container is not running or healthcheck is still pending. | Run `docker compose ps` and `docker compose up -d mysql`. Wait 5 seconds for healthcheck to turn healthy. In a pinch without Docker, activate profile `test` which uses H2 in MySQL mode: `mvn spring-boot:run -Dspring-boot.run.profiles=test`. |
| **Port 8080 Conflict** (`PortInUseException: Web server failed to start. Port 8080 was already in use`) | A leftover Spring Boot instance, local Tomcat, or other process owns 8080. | Kill the existing process: on Windows run `Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess \| Stop-Process -Force`. Alternatively override port on the fly: `--server.port=8081`. |
| **Flyway Checksum Mismatch** (`FlywayException: Validate failed: Migrations have failed validation`) | A developer modified `V1__init.sql` or `V2__seed_branches.sql` after it was already executed against MySQL. | If demo database has throwaway data: `docker compose down -v` followed by `docker compose up -d mysql` to reset volume cleanly. |
| **Bean Definition Override Disabled** | Spring Boot 2.1+ disables silent bean overriding by default (`spring.main.allow-bean-definition-overriding=false`). | Our `CustomDataSourceConfig` and `ResumeStorageAutoConfiguration` rely on `@ConditionalOnMissingBean` rather than bean definition overriding, so they comply with modern Spring standards without needing the flag. |

