# 📚 Book Store Application

⚠️ Developed as part of Week 6 Spring Boot Training.

A production-style RESTful Bookstore application built using **Spring Boot 3**, **Spring Data JPA**, and **MySQL**.  
This project demonstrates layered architecture, REST API design, asynchronous processing, and thread pool management using `ExecutorService`.

---

## 📌 Project Overview

This application implements CRUD operations for managing books and supports concurrent order processing.

It follows standard enterprise layered architecture:

Client → Controller → Service → Repository → Database

---

## 🚀 Features

- ✅ Complete CRUD operations for Books
- ✅ Search books by keyword (name or author)
- ✅ Order management with timestamp tracking
- ✅ Asynchronous email simulation using `@Async`
- ✅ Thread pool implementation using `ExecutorService`
- ✅ Spring Data JPA integration
- ✅ MySQL / H2 database support
- ✅ Clean layered architecture
- ✅ RESTful API principles
- ✅ Transaction management using `@Transactional`

---

## 🛠 Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Hibernate ORM

### Database
- MySQL (Primary)
- H2 (Optional for testing)

### Build Tool
- Maven

### Concepts Used
- Jakarta Persistence API (JPA)
- ExecutorService (Thread Pool)
- Asynchronous Processing
- Dependency Injection
- Transaction Management

---

## 📂 Project Structure
BookStoreApplication/
│
├── controller/ → REST endpoints
├── service/ → Business logic + Thread handling
├── repository/ → Data access layer
├── entity/ → JPA entities
└── BookStoreApplication.java


---

## 🗄 Database Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

⚠️ Never commit real database passwords to version control.

▶️ Running the Application
Using Maven
mvn spring-boot:run
Or Build & Run
mvn clean package
java -jar target/BookStoreApplication-0.0.1-SNAPSHOT.jar

Application runs on:

http://localhost:8080
🌐 REST API Endpoints

Base URL:

http://localhost:8080/books
Method	Endpoint	Description
POST	/books	Create book
GET	/books	Get all books
GET	/books/{id}	Get book by ID
PUT	/books/{id}	Update book
DELETE	/books/{id}	Delete book
GET	/books/search?keyword=java	Search books
POST	/books/order?bookName=name	Order book
🧪 Sample Request (Create Book)
POST /books
Content-Type: application/json
{
  "name": "Effective Java",
  "author": "Joshua Bloch",
  "price": 45.99
}
🧠 Key Concepts Implemented
1️⃣ Layered Architecture

Controller → Handles HTTP requests

Service → Business logic & thread pool management

Repository → Data access via JPA

Entity → Database mapping

2️⃣ Spring Data JPA

Extended JpaRepository

Custom query methods

Automatic CRUD generation

ORM mapping with Hibernate

3️⃣ Asynchronous Processing

Enabled using @EnableAsync

@Async for non-blocking execution

Improves response performance

4️⃣ Thread Pool (ExecutorService)

Reusable threads

Better resource management

Concurrent order processing

Controlled thread lifecycle

5️⃣ Transaction Management

Used @Transactional

Ensures atomic database operations

Automatic rollback on failure

🔮 Future Improvements

Global exception handling (@ControllerAdvice)

Input validation (Bean Validation)

Pagination & sorting

Swagger/OpenAPI documentation

Spring Security integration

Unit & integration testing

Logging framework integration

Docker containerization

CI/CD pipeline setup

👨‍💻 Author

MD Ejazuddin Jamadar
Organization: QBurst
Role: Java Trainee
Focus: Spring Boot, JPA, Multithreading

📚 Learning References

Spring Boot Documentation

Spring Data JPA Guide

Hibernate ORM Documentation

REST API Design Best Practices