# BookStore Application

A RESTful web service built with Spring Boot that demonstrates CRUD operations, Spring Data JPA integration, asynchronous processing with thread pools, and database management for a bookstore system.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Build-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue.svg)](https://www.mysql.com/)

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Key Concepts Implemented](#key-concepts-implemented)
- [Usage Examples](#usage-examples)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

---

## Features

- **Complete CRUD Operations** - Create, Read, Update, and Delete books
- **Advanced Search** - Search books by keyword (name or author) with case-insensitive matching
- **Order Management** - Place book orders with timestamp tracking
- **Asynchronous Processing** - Email notifications using `@Async` annotation
- **Thread Pool Implementation** - Efficient concurrent task execution using ExecutorService
- **Database Persistence** - MySQL integration with Spring Data JPA
- **RESTful API Design** - Clean REST endpoints following industry standards
- **Layered Architecture** - Separation of concerns (Controller, Service, Repository, Entity)
- **Auto-Configuration** - Leverages Spring Boot's convention over configuration
- **Hot Reload** - Development support with Spring Boot DevTools

---

## Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 4.0.3** - Application framework
- **Spring Web MVC** - Web layer
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM framework

### Database
- **MySQL** - Production database
- **H2** - In-memory database (optional for testing)

### Build Tool
- **Maven** - Dependency management and build automation

### Other Technologies
- **Jakarta Persistence API (JPA)** - ORM specification
- **MySQL Connector/J** - JDBC driver
- **Spring Boot DevTools** - Development utilities

---

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
  ```bash
  java -version
  ```

- **Apache Maven 3.6+**
  ```bash
  mvn -version
  ```

- **MySQL Server 8.0+**
  ```bash
  mysql --version
  ```

- **Git** (for cloning the repository)
  ```bash
  git --version
  ```

---

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd BookStoreApplication
```

### 2. Set Up MySQL Database

Create a new database in MySQL:

```sql
CREATE DATABASE bookstore_db;
```

### 3. Configure Database Credentials

Update the `src/main/resources/application.properties` file with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 4. Build the Project

```bash
mvn clean install
```

---

## Configuration

### application.properties

```properties
# Application Configuration
spring.application.name=BookStoreApplication
spring.profiles.active=dev

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=root
spring.datasource.password=Root@1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

### Configuration Options

| Property | Description | Values |
|----------|-------------|--------|
| `spring.jpa.hibernate.ddl-auto` | DDL mode | `create`, `update`, `validate`, `none` |
| `spring.jpa.show-sql` | Show SQL queries in console | `true`, `false` |
| `spring.jpa.properties.hibernate.format_sql` | Format SQL output | `true`, `false` |

---

## Running the Application

### Option 1: Using Maven

```bash
mvn spring-boot:run
```

### Option 2: Using Java

```bash
mvn clean package
java -jar target/BookStoreApplication-0.0.1-SNAPSHOT.jar
```

### Option 3: Using IDE

- Import the project as a Maven project
- Run the `BookStoreApplication.java` main class

The application will start on **http://localhost:8080**

---

## API Documentation

### Base URL
```
http://localhost:8080/books
```

### Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/books` | Create a new book | `Book` JSON | Created `Book` |
| `GET` | `/books` | Get all books | - | List of `Book` |
| `GET` | `/books/{id}` | Get book by ID | - | `Book` |
| `PUT` | `/books/{id}` | Update a book | `Book` JSON | Updated `Book` |
| `DELETE` | `/books/{id}` | Delete a book | - | Success message |
| `GET` | `/books/search?keyword={keyword}` | Search books | - | List of `Book` |
| `POST` | `/books/order?bookName={bookName}` | Order a book | - | Order confirmation |

### Request/Response Examples

#### 1. Create a Book

**Request:**
```http
POST /books
Content-Type: application/json

{
  "name": "Effective Java",
  "author": "Joshua Bloch",
  "price": 45.99
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Effective Java",
  "author": "Joshua Bloch",
  "price": 45.99
}
```

#### 2. Get All Books

**Request:**
```http
GET /books
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Effective Java",
    "author": "Joshua Bloch",
    "price": 45.99
  },
  {
    "id": 2,
    "name": "Clean Code",
    "author": "Robert C. Martin",
    "price": 42.50
  }
]
```

#### 3. Update a Book

**Request:**
```http
PUT /books/1
Content-Type: application/json

{
  "name": "Effective Java (3rd Edition)",
  "author": "Joshua Bloch",
  "price": 49.99
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Effective Java (3rd Edition)",
  "author": "Joshua Bloch",
  "price": 49.99
}
```

#### 4. Search Books

**Request:**
```http
GET /books/search?keyword=java
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Effective Java",
    "author": "Joshua Bloch",
    "price": 45.99
  }
]
```

#### 5. Order a Book

**Request:**
```http
POST /books/order?bookName=Effective Java
```

**Response:**
```text
Order placed for book: Effective Java
```

---

## Project Structure

```
BookStoreApplication/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/qburst/training/bookstoreapplication/
│   │   │       ├── Controller/
│   │   │       │   └── BookController.java          # REST API endpoints
│   │   │       ├── Service/
│   │   │       │   ├── BookService.java            # Business logic
│   │   │       │   └── EmailService.java           # Async email service
│   │   │       ├── Repository/
│   │   │       │   ├── BookRepository.java         # Data access layer
│   │   │       │   └── OrderRepository.java        # Order data access
│   │   │       ├── Entity/
│   │   │       │   ├── Book.java                   # Book entity/model
│   │   │       │   └── Order.java                  # Order entity/model
│   │   │       └── BookStoreApplication.java       # Main application class
│   │   │
│   │   └── resources/
│   │       ├── application.properties              # Configuration file
│   │       ├── static/                             # Static resources
│   │       └── templates/                          # View templates
│   │
│   └── test/
│       └── java/
│           └── com/qburst/training/bookstoreapplication/
│               └── BookStoreApplicationTests.java  # Test cases
│
├── target/                                         # Compiled classes
├── pom.xml                                         # Maven configuration
├── mvnw                                            # Maven wrapper (Unix)
├── mvnw.cmd                                        # Maven wrapper (Windows)
├── HELP.md                                         # Help documentation
└── README.md                                       # This file
```

---

## Database Schema

### Books Table

| Column | Type | Constraints |
|--------|------|-------------|
| `id` | `BIGINT` | Primary Key, Auto Increment |
| `name` | `VARCHAR(255)` | NOT NULL |
| `author` | `VARCHAR(255)` | - |
| `price` | `DOUBLE` | - |

### Orders Table

| Column | Type | Constraints |
|--------|------|-------------|
| `id` | `BIGINT` | Primary Key, Auto Increment |
| `book_name` | `VARCHAR(255)` | NOT NULL |
| `order_date` | `DATETIME` | - |

---

## Key Concepts Implemented

### 1. **Layered Architecture**
```
┌─────────────────────┐
│   Controller Layer  │  ← REST endpoints
├─────────────────────┤
│   Service Layer     │  ← Business logic
├─────────────────────┤
│  Repository Layer   │  ← Data access
├─────────────────────┤
│   Entity Layer      │  ← Data models
├─────────────────────┤
│     Database        │  ← MySQL
└─────────────────────┘
```

### 2. **Dependency Injection**
- Constructor-based dependency injection
- Loose coupling between layers
- Enhanced testability

### 3. **Spring Data JPA**
- Repository interface extending `JpaRepository`
- Custom JPQL queries using `@Query`
- Automatic CRUD operations
- No boilerplate data access code

### 4. **Asynchronous Processing**
- `@EnableAsync` for asynchronous method execution
- `@Async` annotation on email service
- Non-blocking order confirmation

### 5. **Thread Pool (ExecutorService)**
- Concurrent task execution
- Thread reuse and lifecycle management
- Improved performance for order processing

### 6. **RESTful Design Principles**
- Resource-based URLs
- Proper HTTP methods (GET, POST, PUT, DELETE)
- JSON request/response format
- Stateless communication

### 7. **JPA Annotations**
- `@Entity` - JPA entity
- `@Table` - Table mapping
- `@Id` - Primary key
- `@GeneratedValue` - Auto-increment
- `@Column` - Column constraints

---

## Usage Examples

### Using cURL

#### Create a Book
```bash
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring in Action",
    "author": "Craig Walls",
    "price": 39.99
  }'
```

#### Get All Books
```bash
curl -X GET http://localhost:8080/books
```

#### Search Books
```bash
curl -X GET "http://localhost:8080/books/search?keyword=spring"
```

#### Update a Book
```bash
curl -X PUT http://localhost:8080/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring in Action (6th Edition)",
    "author": "Craig Walls",
    "price": 44.99
  }'
```

#### Delete a Book
```bash
curl -X DELETE http://localhost:8080/books/1
```

#### Order a Book
```bash
curl -X POST "http://localhost:8080/books/order?bookName=Spring in Action"
```

### Using Postman

1. Import the API endpoints
2. Set the base URL to `http://localhost:8080`
3. Add appropriate headers: `Content-Type: application/json`
4. Send requests with JSON body for POST/PUT operations

---

## Screenshots

For detailed screenshots and demonstrations, please visit:
[Google Drive - Screenshots](https://drive.google.com/drive/folders/1Z8_wu3l2gV4QGetnWrp9onkv6Yo-UHON)

---

## Development

### Building for Production

```bash
mvn clean package -DskipTests
```

### Running Tests

```bash
mvn test
```

### Code Formatting

Follow Java coding conventions and Spring Boot best practices.

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## License

This project is created for educational purposes.

---

## Author

**Ejazuddin**
- Organization: QBurst

---

## Acknowledgments

- Spring Framework Documentation
- Java Community

---

## Learning Resources

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [RESTful API Design Best Practices](https://restfulapi.net/)
- [Hibernate ORM](https://hibernate.org/orm/)

---

## Known Issues

None currently. Please report any issues in the repository.

---

## Future Enhancements

- [ ] Add global exception handling with `@ControllerAdvice`
- [ ] Implement input validation using Bean Validation API
- [ ] Add pagination and sorting
- [ ] Implement authentication and authorization (Spring Security)
- [ ] Add comprehensive unit and integration tests
- [ ] Implement API documentation with Swagger/OpenAPI
- [ ] Add caching mechanism (Redis/Caffeine)
- [ ] Implement logging framework (SLF4J + Logback)
- [ ] Create Docker containerization
- [ ] Set up CI/CD pipeline

---

**Built with Spring Boot**
