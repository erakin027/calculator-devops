# calculator-devops

**CSE 816 — Software Production Engineering Mini-Project**

**Name:** Abhijit Dibbidi  
**Roll Number:** IMT2023054

A Calculator REST API built using Java 17, Spring Boot 3, and Maven, with Docker-based deployment and a Jenkins CI/CD pipeline.

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

### Example success response

```json
{
  "operation": "add",
  "a": 2.0,
  "b": 3.0,
  "result": 5.0
}
```

### Example error response

HTTP `400 Bad Request`

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Division by zero is not allowed",
  "timestamp": "..."
}
```

## Build and Test

Build the application and run the tests:

```bash
mvn clean package
mvn test
```

The packaged application is generated at `target/calculator.jar`. Test reports are available in `target/surefire-reports/`.

## Run Locally

Start the application:

```bash
java -jar target/calculator.jar
```

The application listens on port `8080` by default. To use a different port:

```bash
java -jar target/calculator.jar --server.port=8081
```

### Example API requests

```bash
curl "http://localhost:8080/api/v1/add?a=2&b=3"
curl "http://localhost:8080/api/v1/multiply?a=4&b=3"
curl "http://localhost:8080/api/v1/divide?a=6&b=3"
curl -i "http://localhost:8080/api/v1/divide?a=6&b=0"
curl "http://localhost:8080/actuator/health"
curl "http://localhost:8080/actuator/info"
```

Replace port `8080` with the port you selected when starting the application.

## Docker

Build the Docker image:

```bash
docker build -t calculator-devops .
```

Run the container:

```bash
docker run -d -p 8082:8080 --name calc calculator-devops
```

Test the health endpoint:

```bash
curl "http://localhost:8082/actuator/health"
```

The container's port `8080` is published on host port `8082`, avoiding a conflict with Jenkins on host port `8080`.

Stop and remove the container when finished:

```bash
docker stop calc
docker rm calc
```

## Docker Compose — Nginx and Calculator Replicas

Docker Compose deploys Nginx as a reverse proxy in front of two calculator API replicas.

Start the services:

```bash
docker compose up -d
```

Check their status:

```bash
docker compose ps
```

Send a request through Nginx:

```bash
curl "http://localhost:8081/api/v1/multiply?a=4&b=3"
```

Check application health through the configured endpoint:

```bash
curl "http://localhost:8081/actuator/health"
```

Stop the services:

```bash
docker compose down
```

Nginx exposes the application on host port `8081`, keeping it separate from Jenkins on port `8080`.

## CI/CD Pipeline

The `Jenkinsfile` defines the following pipeline:

**Checkout → Build → Test → Docker Build → Push → Deploy**

The pipeline:

1. Checks out the source code from GitHub.
2. Builds the application using Maven.
3. Runs automated tests and publishes test reports.
4. Builds a Docker image tagged with the Jenkins build number and `latest`.
5. Pushes the images to Docker Hub.
6. Deploys the application using Docker Compose.
7. Checks the application's health endpoint.
8. Sends an email notification on build success or failure.

A GitHub webhook triggers the Jenkins pipeline when configured and enabled.

### Docker Hub Image

The pipeline publishes images using the following tag format:

```text
<dockerhub-username>/calculator-devops:<build-number>
<dockerhub-username>/calculator-devops:latest
```

Replace `<dockerhub-username>` with the Docker Hub account used by the pipeline.

## Technologies Used

- Java 17
- Spring Boot 3
- Maven
- JUnit
- Docker and Docker Compose
- Nginx
- Jenkins
- Git and GitHub
- Docker Hub