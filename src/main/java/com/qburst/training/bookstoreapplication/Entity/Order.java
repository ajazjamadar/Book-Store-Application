package com.qburst.training.bookstoreapplication.Entity;

import java.time.LocalDateTime;

import com.qburst.training.bookstoreapplication.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Order() {
        this.orderDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public Order(Book book) {
        this.book = book;
        this.orderDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
}
