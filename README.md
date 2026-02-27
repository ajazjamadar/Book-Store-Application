# 📚 BookStore Application

A full-stack bookstore application built with **Spring Boot** (backend) and a static HTML/CSS/JS frontend, backed by **MySQL**, containerized using **Docker** and orchestrated with **Docker Compose**.

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Architecture & Request Flow](#-architecture--request-flow)
- [Backend Package Details](#-backend-package-details)
- [Frontend Structure](#-frontend-structure)
- [Docker & Infrastructure](#-docker--infrastructure)
- [Database Migrations](#-database-migrations)
- [Environment Configuration](#-environment-configuration)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Scripts](#-scripts)

---

## 🌐 Project Overview

BookStore Application is a full-stack e-commerce platform for managing and purchasing books online.

### Features
- 📖 Book Management (CRUD)
- 🛒 Order Management with Payment Processing
- 💳 Multiple Payment Methods (Credit Card, Debit Card, UPI)
- 🔍 Search & Filter Books
- 📧 Email Notifications via SendGrid
- 🗄️ MySQL database with Docker persistence
- 🐳 Fully Dockerized for easy deployment

---

## 🛠 Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Backend      | Java 21, Spring Boot 4.x          |
| ORM          | Spring Data JPA, Hibernate        |
| Database     | MySQL 8.0                         |
| Email        | SendGrid                          |
| Frontend     | Static HTML / CSS / JavaScript    |
| Web Server   | Nginx                             |
| Build Tool   | Maven                             |
| Container    | Docker, Docker Compose            |

---

## 📁 Project Structure

```
bookstore-application/
│
├── src/                                          ← Java source code (Spring Boot backend)
│   └── main/
│       ├── java/com/qburst/training/bookstoreapplication/
│       │   ├── Config/                           ← Spring configuration classes
│       │   ├── Controller/                       ← REST API controllers
│       │   ├── Service/                          ← Service classes (business logic)
│       │   ├── Repository/                       ← JPA repositories (DB access)
│       │   ├── Entity/                           ← JPA entities (DB table mappings)
│       │   ├── Dto/                              ← Data Transfer Objects
│       │   ├── Factory/                          ← Payment processor factory pattern
│       │   ├── enums/                            ← Enum constants
│       │   ├── exception/                        ← Custom exceptions & global handler
│       │   └── BookStoreApplication.java         ← Main entry point
│       │
│       └── resources/
│           ├── application.properties            ← Application configuration
│           └── static/                           ← Frontend static assets (HTML/CSS/JS)
│
├── backend/                                      ← Backend build & Docker files
│   ├── pom.xml                                   ← Maven dependencies & build config
│   ├── Dockerfile                                ← Docker image for Spring Boot
│   ├── mvnw / mvnw.cmd                           ← Maven wrapper scripts
│   └── .mvn/                                     ← Maven wrapper config
│
├── frontend/                                     ← Frontend application
│   ├── public/                                   ← Static assets served by Nginx
│   │   ├── index.html                            ← Main HTML page
│   │   ├── css/style.css                         ← Application styles
│   │   └── js/app.js                             ← Frontend JavaScript logic
│   ├── src/                                      ← Frontend source (future framework)
│   ├── package.json                              ← Node dependencies & scripts
│   └── Dockerfile                                ← Docker image for frontend/Nginx
│
├── docker/                                       ← Docker infrastructure configs
│   ├── nginx/
│   │   └── nginx.conf                            ← Nginx reverse proxy config
│   └── database/
│       └── init.sql                              ← Initial DB schema & seed data
│
├── docker-compose.yml                            ← Orchestrates all services
├── .env                                          ← Active environment variables (not committed)
├── .env.example                                  ← Template for required env variables
├── .gitignore                                    ← Files ignored by Git
├── README.md                                     ← Project documentation
└── scripts/                                      ← Automation shell scripts
    └── start-dev.sh                              ← Start app in development mode
```

---

## 🏗 Architecture & Request Flow

```
Client (Browser)
      │
      ▼ HTTP Request
┌─────────────┐
│    Nginx    │  ← Serves frontend static files, proxies /books/** to backend
└─────────────┘
      │ /books/**
      ▼
┌─────────────────────────┐
│   GlobalExceptionHandler │  ← Catches all exceptions, returns uniform JSON
└─────────────────────────┘
      │
      ▼
┌─────────────┐
│ Controller  │  ← Receives HTTP request, delegates to service
└─────────────┘
      │
      ▼
┌─────────────┐
│   Service   │  ← Business logic, payment processing, email notifications
└─────────────┘
      │
      ▼
┌────────────┐
│ Repository │  ← Spring Data JPA queries to database
└────────────┘
      │
      ▼
┌──────────┐
│  MySQL   │  ← Persistent data store
└──────────┘
      │ returns Entity
      ▼
DTO / ApiResponse<T> → Controller → JSON Response → Client
```

---

## 📦 Backend Package Details

### `BookStoreApplication.java`
> Main entry point of the Spring Boot application.
- Annotated with `@SpringBootApplication` and `@EnableAsync`
- Starts the embedded Tomcat server
- `@EnableAsync` enables background email sending

---

### `Config/`
> Spring configuration classes

| Class | Purpose |
|-------|---------|
| `AppConfiguration.java` | Singleton bean — binds `app.*` properties; holds app name, version, description |
| `CorsConfig.java` | Configures allowed CORS origins using the `app.frontend.url` property |

---

### `Controller/`
> REST API layer — handles HTTP requests and returns responses

| Class | Endpoint Prefix | Purpose |
|-------|-----------------|---------|
| `BookController.java` | `/books` | Full CRUD for books, payment endpoints, order endpoint, inbox check |

**Endpoints:**

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/books` | List all books |
| `GET` | `/books/{id}` | Get book by ID |
| `POST` | `/books` | Create a book |
| `PUT` | `/books/{id}` | Update a book |
| `DELETE` | `/books/{id}` | Delete a book |
| `GET` | `/books/search?keyword=` | Search books by name or author |
| `POST` | `/books/process-payment/card` | Process credit/debit card payment |
| `POST` | `/books/process-payment/upi` | Process UPI payment |
| `POST` | `/books/order?bookName=` | Place a simple order (no payment) |
| `GET` | `/books/inbox` | Check TestMail inbox |

---

### `Service/`
> Business logic layer — sits between controllers and repositories

| Class | Purpose |
|-------|---------|
| `BookService.java` | Book CRUD, payment processing orchestration, order creation |
| `OrderService.java` | Creates orders in DB, updates payment status |
| `EmailService.java` | Sends order confirmation emails via SendGrid asynchronously |

---

### `Repository/`
> Data access layer using Spring Data JPA

| Class | Table | Purpose |
|-------|-------|---------|
| `BookRepository.java` | `books` | CRUD + `findByNameIgnoreCase` + keyword search query |
| `OrderRepository.java` | `orders` | Find orders by book or payment status |

---

### `Entity/`
> JPA Entity classes — map directly to database tables

| Class | Table | Description |
|-------|-------|-------------|
| `Book.java` | `books` | Stores book details: name, author, price |
| `Order.java` | `orders` | Stores order details: book reference, order date, payment status |

---

### `Dto/`
> Data Transfer Objects — shapes of data sent to/from the API

| Class | Direction | Description |
|-------|-----------|-------------|
| `ApiResponse<T>` | Response | Generic wrapper `{ success, message, data }` for all endpoints |
| `PaymentRequest.java` | Request (abstract) | Base class: bookName, amount, paymentType |
| `CardPaymentRequest.java` | Request | Extends PaymentRequest — adds cardNumber, cvv (write-only), expiryDate, cardHolderName |
| `UpiPaymentRequest.java` | Request | Extends PaymentRequest — adds upiId |
| `PaymentResponse.java` | Response | Returns status, message, transactionId, amount, paymentMethod, orderId, timestamp |

---

### `Factory/`
> Factory design pattern — creates the right payment processor based on payment type

| Class | Purpose |
|-------|---------|
| `PaymentProcessor.java` | Interface — defines `processPayment()` and `getType()` |
| `PaymentProcessorFactory.java` | Auto-discovers all processors via Spring DI, resolves by type string |
| `CreditCardProcessor.java` | Processes credit card payments — validates card number, CVV, expiry |
| `DebitCardProcessor.java` | Processes debit card payments — same validation as credit card |
| `UpiProcessor.java` | Processes UPI payments — validates UPI ID format (`user@bank`) |

---

### `enums/`
> Enum constants used throughout the application

| Enum | Values | Used In |
|------|--------|---------|
| `PaymentStatus.java` | `PENDING`, `SUCCESS`, `FAILED` | `Order` entity, `PaymentResponse` |

---

### `exception/`
> Custom exceptions and global error handling

| Class | Purpose |
|-------|---------|
| `ResourceNotFoundException.java` | Thrown when a book/order ID doesn't exist in the DB |
| `GlobalExceptionHandler.java` | `@RestControllerAdvice` — catches all exceptions and returns uniform `ApiResponse` JSON errors |

**Handled exceptions:**

| Exception | HTTP Status |
|-----------|-------------|
| `ResourceNotFoundException` | `404 Not Found` |
| `MethodArgumentNotValidException` | `400 Bad Request` (with field-level errors) |
| `IllegalArgumentException` | `400 Bad Request` |
| `HttpRequestMethodNotSupportedException` | `405 Method Not Allowed` |
| `NoResourceFoundException` | `404 Not Found` |
| `Exception` (catch-all) | `500 Internal Server Error` |

---

## 🎨 Frontend Structure

```
frontend/
├── public/
│   ├── index.html        ← Main single-page HTML — book listing, add/edit forms, payment forms
│   ├── css/
│   │   └── style.css     ← All application styles
│   └── js/
│       └── app.js        ← JavaScript: API calls, DOM manipulation, form handling
└── src/                  ← Reserved for future frontend framework (React/Vue/Angular)
```

---

## 🐳 Docker & Infrastructure

### `docker-compose.yml`
Orchestrates all three services together:

| Service | Description | Port |
|---------|-------------|------|
| `db` | MySQL 8.0 database with persistent volume | `3306` (internal) |
| `backend` | Spring Boot application | `8080` (internal) |
| `frontend` | Static files served by Nginx | `80` (public) |

**Service startup order:** `db` → `backend` (waits for db healthcheck) → `frontend` (waits for backend healthcheck)

---

### `docker/nginx/nginx.conf`
- Serves the `frontend/public/` static files
- Proxies all `/books/**` requests to the backend service on port 8080
- Adds security headers (`X-Frame-Options`, `X-Content-Type-Options`, etc.)
- Disables server version disclosure (`server_tokens off`)

---

### `docker/database/init.sql`
- Runs automatically on first database container start
- Creates `bookstore_db` database
- Creates `books` and `orders` tables

---

### `backend/Dockerfile`
Two-stage Docker build:
- **Stage 1 (builder):** Uses `maven:3.9-eclipse-temurin-21-alpine` — downloads dependencies then builds the JAR
- **Stage 2 (runtime):** Uses `eclipse-temurin:21-jre-alpine` — runs the JAR as a non-root user

---

## 🗄 Database Migrations

Reference SQL scripts are stored in `backend/src/main/resources/db/migration/`:

```
db/migration/
└── V1__init_schema.sql     ← Creates books and orders tables
```

> The application currently uses `spring.jpa.hibernate.ddl-auto=update` in development
> so tables are auto-managed by Hibernate. The migration scripts serve as reference and
> are also applied via `docker/database/init.sql` during Docker startup.

---

## ⚙ Environment Configuration

Three YAML configuration files control the application behaviour:

### `application.yml` — Base (shared)
```yaml
spring:
  application:
    name: BookStoreApplication
  profiles:
    active: dev          # overridden by SPRING_PROFILES_ACTIVE env var
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

app:
  name: BookStore Application
  version: 1.0.0
  description: Online Book Store with Payment Processing
```

### `application-dev.yml` — Development
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookstore_db
    username: root
    password: Root@1234
  jpa:
    show-sql: true

app:
  frontend:
    url: http://localhost:8080

sendgrid.api.key: <your-key>
email:
  from: no-reply@bookstore.com
  to: your-inbox@testmail.app
```

### `application-prod.yml` — Production
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}

app:
  frontend:
    url: ${APP_FRONTEND_URL}

sendgrid.api.key: ${SENDGRID_API_KEY}
email:
  from: ${EMAIL_FROM}
  to: ${EMAIL_TO}
```

### `.env.example`
```env
# Database
MYSQL_ROOT_PASSWORD=Root@1234

# Email (SendGrid)
SENDGRID_API_KEY=your-sendgrid-api-key
EMAIL_FROM=no-reply@bookstore.com
EMAIL_TO=your-inbox@testmail.app

# TestMail
TESTMAIL_API_KEY=your-testmail-api-key
TESTMAIL_NAMESPACE=your-namespace
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose

---

### Option 1: Run with Docker Compose (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/your-org/bookstore-application.git
cd bookstore-application

# 2. Copy and configure environment variables
cp .env.example .env
# Edit .env with your values

# 3. Start all services
docker compose up --build

# 4. Access the application
#    Frontend:    http://localhost
#    Backend API: http://localhost (proxied through Nginx)
```

---

### Option 2: Run Backend Locally

```bash
# 1. Ensure MySQL is running locally with database 'bookstore_db'

# 2. Run with Maven wrapper
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Backend API available at: http://localhost:8080
```

---

## 📡 API Endpoints

### Books
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/books` | Get all books |
| `GET` | `/books/{id}` | Get book by ID |
| `POST` | `/books` | Create a new book |
| `PUT` | `/books/{id}` | Update a book |
| `DELETE` | `/books/{id}` | Delete a book |
| `GET` | `/books/search?keyword=` | Search by name or author |

### Payments
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/books/process-payment/card` | Process credit or debit card payment |
| `POST` | `/books/process-payment/upi` | Process UPI payment |

**Card Payment Request Body:**
```json
{
  "bookName": "Clean Code",
  "amount": 29.99,
  "paymentType": "credit",
  "cardNumber": "4111111111111111",
  "cvv": "123",
  "expiryDate": "12/27",
  "cardHolderName": "John Doe"
}
```

**UPI Payment Request Body:**
```json
{
  "bookName": "Clean Code",
  "amount": 29.99,
  "paymentType": "upi",
  "upiId": "john@paytm"
}
```

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/books/order?bookName=` | Place a simple order (triggers email) |

### Utility
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/books/inbox` | Fetch TestMail inbox (verify emails during dev) |

---

## 📜 Scripts

```
scripts/
└── start-dev.sh      ← Starts the backend in development mode
```

### Usage
```bash
# Make executable
chmod +x scripts/start-dev.sh

# Start in development mode
./scripts/start-dev.sh
```

---

## 🧪 Running Tests

```bash
cd backend
./mvnw test
```

---

> Built by MD EJAZUDDIN JAMADAR
>          Trainee @Qburst Bengaluru
