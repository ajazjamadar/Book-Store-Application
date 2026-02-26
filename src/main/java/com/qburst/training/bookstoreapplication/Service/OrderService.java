package com.qburst.training.bookstoreapplication.Service;

import org.springframework.stereotype.Service;

import com.qburst.training.bookstoreapplication.Entity.Order;
import com.qburst.training.bookstoreapplication.Repository.OrderRepository;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;
import com.qburst.training.bookstoreapplication.exception.ResourceNotFoundException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String bookName) {
        Order order = new Order(bookName);
        order.setPaymentStatus(PaymentStatus.PENDING);
        return orderRepository.save(order);
    }

    public void updatePaymentStatus(Long orderId, PaymentStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setPaymentStatus(status);
        orderRepository.save(order);
    }
}
