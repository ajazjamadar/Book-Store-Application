package com.qburst.training.bookstoreapplication.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qburst.training.bookstoreapplication.Entity.Book;
import com.qburst.training.bookstoreapplication.Entity.Order;
import com.qburst.training.bookstoreapplication.Repository.OrderRepository;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;
import com.qburst.training.bookstoreapplication.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Book book) {
        Order order = new Order(book);  // status defaults to PENDING in constructor
        return orderRepository.save(order);
    }

    @Transactional
    public void updatePaymentStatus(Long orderId, PaymentStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setPaymentStatus(status);
        orderRepository.save(order);
    }
}
