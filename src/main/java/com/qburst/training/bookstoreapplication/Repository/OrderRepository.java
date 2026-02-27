package com.qburst.training.bookstoreapplication.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qburst.training.bookstoreapplication.Entity.Book;
import com.qburst.training.bookstoreapplication.Entity.Order;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBook(Book book);

    List<Order> findByPaymentStatus(PaymentStatus status);
}
