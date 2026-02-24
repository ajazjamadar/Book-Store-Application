package com.qburst.training.bookstoreapplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qburst.training.bookstoreapplication.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
