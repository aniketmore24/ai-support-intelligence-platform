# AI Support Intelligence Platform

A production-style Spring Boot backend project for intelligent support ticket processing using AI, Kafka, Redis, and PostgreSQL.

This project demonstrates real-world backend architecture with asynchronous event-driven processing, database persistence, caching, and AI-powered ticket classification.

---

## Features

* Create and manage support tickets
* AI-powered ticket classification using OpenRouter (LLM integration)
* Automatic classification of:

  * Category
  * Sentiment
  * Priority
* Asynchronous processing using Apache Kafka
* PostgreSQL persistence using Spring Data JPA + Hibernate
* Redis caching for faster repeated lookups
* Pagination and filtering APIs
* DTO-based clean API response design
* Transaction management with `@Transactional`
* Dockerized infrastructure setup

---

## Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Cache
* Spring Kafka
* WebClient

### Database & Messaging

* PostgreSQL
* Redis
* Apache Kafka
* Zookeeper

### DevOps / Tools

* Docker
* Docker Compose
* Postman
* Git + GitHub
* Maven

### AI Integration

* OpenRouter API
* LLM-based ticket analysis

---

## Architecture

```text
POST /api/tickets
        ↓
Save Ticket in PostgreSQL
(status = NEW)
        ↓
Publish Kafka Event
(ticket-created)
        ↓
Return Immediate Response

-------------------------------

Kafka Consumer
        ↓
Receive Event
        ↓
Call AI via OpenRouter
        ↓
Extract AI Response
        ↓
Update Ticket
(status = ANALYZED)
```

This improves:

* performance
* scalability
* fault tolerance
* user experience

---

## Project Structure

```text
src/main/java
 ├── controller
 ├── service
 ├── repository
 ├── model
 ├── dto
 ├── event
 ├── aiClient
 ├── config
 └── application
```

---

## API Examples

### Create Ticket

### POST `/api/tickets`

```json
{
  "description": "My internet is not working since morning"
}
```

### Response

```json
{
  "id": 1,
  "description": "My internet is not working since morning",
  "status": "NEW"
}
```

---

### Get All Tickets

### GET `/api/tickets?page=0&size=10`

Supports:

* pagination
* sorting
* filtering

---

### Filter by Priority

### GET `/api/tickets/filter?priority=High&page=0&size=10`

---

## Database Setup

### PostgreSQL (Docker)

```bash
docker run --name postgres-db \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=ai_support_db \
  -p 5432:5432 \
  -d postgres
```

---

## Redis Setup

```bash
docker run --name redis-server \
  -p 6379:6379 \
  -d redis
```

---

## Kafka Setup

Use `docker-compose.yml`

```bash
docker-compose up -d
```

This starts:

* Kafka
* Zookeeper

---

## Application Properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_support_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.redis.host=localhost
spring.redis.port=6379

spring.kafka.bootstrap-servers=localhost:9092

openai.api.url=https://openrouter.ai/api/v1/chat/completions
openai.api.key=YOUR_OPENROUTER_API_KEY
```

---

## Running the Project

```bash
mvn clean install
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

## Future Enhancements

* Retry + Dead Letter Queue (DLQ)
* Swagger / OpenAPI Documentation
* JWT Security / Keycloak Integration
* Microservice split
* React Admin Dashboard
* Notification Service
* Elasticsearch / Semantic Search
* Monitoring + Logging
* CI/CD Pipeline
* Kubernetes Deployment



## Author

Built by Aniket More

Focused on building production-grade backend systems using Java, Spring Boot, Microservices, Kafka, Redis, PostgreSQL, and AI integrations.
