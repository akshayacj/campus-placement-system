# Spring Boot Actuator Cheatsheet: Live Tech Talk Reference

Spring Boot Actuator provides production-ready operational endpoints for monitoring, auditing, and inspecting internal container state at runtime.

---

## 1. Quick Reference Table

| Endpoint | HTTP Method | Tech Talk Role / What It Shows |
| :--- | :--- | :--- |
| `/actuator/health` | `GET` | Overall application health + custom components (`db`, `resumeStorage`, disk space, probes). |
| `/actuator/conditions` | `GET` | **Live equivalent of `--debug`**. Displays full ConditionEvaluationReport (positive, negative, unconditional). |
| `/actuator/beans` | `GET` | Complete hierarchy of beans registered in ApplicationContext, their dependencies, types, and scopes. |
| `/actuator/configprops` | `GET` | All `@ConfigurationProperties` beans, their prefixes, bound values, and sanitization/masking status. |
| `/actuator/env` | `GET` | Loaded `PropertySources` (system environment, system properties, active profile YAMLs) and resolution precedence. |
| `/actuator/mappings` | `GET` | Full catalog of MVC `@RequestMapping` routes, handler methods, and expected media types. |
| `/actuator/metrics/jvm.memory.used` | `GET` | Live JVM memory consumption metrics across heap and non-heap pools. |

---

## 2. Curl Commands & Sample Invocations

### 1. `/actuator/health`
Checks overall status and custom health indicators.
```bash
curl -s http://localhost:8080/actuator/health | jq .
```
**Sample Output:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 512000000000,
        "free": 245000000000,
        "threshold": 10485760
      }
    },
    "resumeStorage": {
      "status": "UP",
      "details": {
        "type": "LOCAL_DISK",
        "path": "C:\\Users\\appuh\\Tech Talk Project\\target\\storage-demo",
        "writable": true,
        "usableSpaceBytes": 245000000000
      }
    }
  },
  "groups": [
    "liveness",
    "readiness"
  ]
}
```

---

### 2. `/actuator/conditions` (The Live Equivalent of `--debug`)
Inspect why beans were auto-configured or why they backed off at runtime without restarting the app.
```bash
curl -s http://localhost:8080/actuator/conditions | jq .contexts.application.positiveMatches."DataSourceAutoConfiguration"
```
**Why this matters during a live demo:**
- You do not need to scroll through giant console logs.
- Search directly for `CustomDataSourceConfig` or `ResumeStorageAutoConfiguration` to show `@ConditionalOnMissingBean` or `@ConditionalOnProperty` verdicts live in front of the audience.

---

### 3. `/actuator/beans`
View all beans registered in the Spring IoC container.
```bash
curl -s http://localhost:8080/actuator/beans | jq '.contexts.application.beans | keys | .[0:10]'
```
**Filter specifically for our project beans:**
```bash
curl -s http://localhost:8080/actuator/beans | jq '.contexts.application.beans | to_entries[] | select(.key | contains("placement") or contains("Demo") or contains("Controller")) | {name: .key, scope: .value.scope, type: .value.type}'
```

---

### 4. `/actuator/configprops`
Inspect prefix-bound `@ConfigurationProperties` and verify relaxed binding.
```bash
curl -s http://localhost:8080/actuator/configprops | jq '.contexts.application.beans."com.college.placement.config.PlacementProperties"'
```
**Sample Output:**
```json
{
  "prefix": "placement",
  "properties": {
    "email": {
      "allowedDomain": "college.edu.in"
    },
    "policy": {
      "offerResponseWindowHours": 72,
      "dreamCtcThresholdLpa": 10.0,
      "blockRegularAfterRegularOffer": true,
      "dreamAllowedAfterRegular": true
    },
    "jwt": {
      "accessTokenMinutes": 15,
      "refreshTokenDays": 7,
      "secret": "******"
    },
    "storage": {
      "enabled": true,
      "baseDir": "./target/storage-demo",
      "maxResumeSizeBytes": 2097152
    }
  }
}
```

---

### 5. `/actuator/env`
Inspect externalized environment values and property source precedence.
```bash
curl -s http://localhost:8080/actuator/env | jq '.propertySources[] | {name: .name}'
```
**Query a single property to see which source won:**
```bash
curl -s http://localhost:8080/actuator/env/placement.policy.offer-response-window-hours | jq .
```

---

### 6. `/actuator/mappings`
See every registered HTTP endpoint and DispatcherServlet mapping.
```bash
curl -s http://localhost:8080/actuator/mappings | jq '.contexts.application.mappings.dispatcherServlets.dispatcherServlet[] | {predicate: .predicate, handler: .handler}'
```

---

### 7. `/actuator/metrics/jvm.memory.used`
Live JVM heap and non-heap memory usage telemetry.
```bash
curl -s http://localhost:8080/actuator/metrics/jvm.memory.used | jq .
```
**Sample Output:**
```json
{
  "name": "jvm.memory.used",
  "description": "The amount of used memory",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 142548224
    }
  ],
  "availableTags": [
    {
      "tag": "area",
      "values": ["heap", "nonheap"]
    },
    {
      "tag": "id",
      "values": ["G1 Survivor Space", "G1 Old Gen", "Metaspace", "G1 Eden Space"]
    }
  ]
}
```

