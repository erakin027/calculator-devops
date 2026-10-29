# calculator-devops

Calculator REST API built using Java 17, Spring Boot 3, and Maven.

**CSE 816 Software Production Engineering — Mini-Project**

## Endpoints

| Operation | Endpoint |
|---|---|
| Add | `GET /api/v1/add?a=2&b=3` |
| Subtract | `GET /api/v1/subtract?a=5&b=3` |
| Multiply | `GET /api/v1/multiply?a=4&b=3` |
| Divide | `GET /api/v1/divide?a=6&b=3` |
| Health | `GET /actuator/health` |
| Info | `GET /actuator/info` |

Division by zero returns HTTP `400 Bad Request`.

**Success response:**

```json
{
  "operation": "add",
  "a": 2.0,
  "b": 3.0,
  "result": 5.0
}
```

**Error response (HTTP 400):**

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Division by zero is not allowed",
  "timestamp": "..."
}
```

## Build and test

```bash
mvn clean package
mvn test
```

The packaged application is generated at `target/calculator.jar`. Test reports are available in `target/surefire-reports/`.

## Run locally

```bash
java -jar target/calculator.jar
```

The application listens on port `8080` by default. If that port is occupied, you can use another port:

```bash
java -jar target/calculator.jar --server.port=8081
```

## Example API requests

```bash
curl "http://localhost:8080/api/v1/add?a=2&b=3"
curl "http://localhost:8080/api/v1/multiply?a=4&b=3"
curl "http://localhost:8080/api/v1/divide?a=6&b=3"
curl -i "http://localhost:8080/api/v1/divide?a=6&b=0"
curl "http://localhost:8080/actuator/health"
```

Replace port `8080` with `8081` in these URLs if you started the application on port `8081`.