BookStore Application

⚠️ Developed as part of Week 6 Spring Boot training.
This project demonstrates REST API development, Spring Data JPA integration, layered architecture, asynchronous processing, and thread pool management using ExecutorService.

📌 Project Overview

A production-style RESTful Bookstore application built using Spring Boot.
It implements CRUD operations, database persistence with JPA & Hibernate, and concurrent order processing using Java thread pools.

This project follows a standard enterprise layered architecture:

Client → Controller → Service → Repository → Database
🚀 Features

✅ Complete CRUD operations for Books

✅ Search books by keyword (name or author)

✅ Order management with timestamp tracking

✅ Asynchronous email simulation using @Async

✅ Thread pool implementation using ExecutorService

✅ Spring Data JPA integration

✅ MySQL / H2 database support

✅ Clean layered architecture

✅ RESTful API design principles

✅ Transaction management using @Transactional

🛠 Tech Stack
Backend

Java 17

Spring Boot 3.x

Spring Web (REST)

Spring Data JPA

Hibernate ORM

Database

MySQL (Primary)

H2 (Optional for testing)

Build Tool

Maven

Other Concepts

Jakarta Persistence API (JPA)

ExecutorService (Thread Pool)

Asynchronous Processing

Dependency Injection

📂 Project Structure
BookStoreApplication/
│
├── Controller/        → REST endpoints
├── Service/           → Business logic + Thread handling
├── Repository/        → Data access layer
├── Entity/            → JPA entities
└── BookStoreApplication.java
🗄 Database Configuration

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

⚠️ Do not commit real database passwords to version control.

▶️ Running the Application
Using Maven
mvn spring-boot:run
Or Build and Run
mvn clean package
java -jar target/BookStoreApplication-0.0.1-SNAPSHOT.jar

Application runs on:

http://localhost:8080
🌐 REST API Endpoints
Base URL
http://localhost:8080/books
Method	Endpoint	Description
POST	/books	Create book
GET	/books	Get all books
GET	/books/{id}	Get book by ID
PUT	/books/{id}	Update book
DELETE	/books/{id}	Delete book
GET	/books/search?keyword=java	Search books
POST	/books/order?bookName=name	Order book
📌 Key Concepts Implemented
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

🧪 Sample Request (Create Book)
POST /books
Content-Type: application/json

{
"name": "Effective Java",
"author": "Joshua Bloch",
"price": 45.99
}
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