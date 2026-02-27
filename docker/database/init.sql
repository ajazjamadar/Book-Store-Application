-- BookStore Application — Docker MySQL Initialisation
-- This file is mounted at /docker-entrypoint-initdb.d/ and runs once on first startup.

CREATE DATABASE IF NOT EXISTS bookstore_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookstore_db;

CREATE TABLE IF NOT EXISTS books (
    id     BIGINT       NOT NULL AUTO_INCREMENT,
    name   VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    price  DECIMAL(10, 2),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS orders (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    book_id        BIGINT       NOT NULL,
    order_date     DATETIME(6),
    payment_status VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_book FOREIGN KEY (book_id) REFERENCES books (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
