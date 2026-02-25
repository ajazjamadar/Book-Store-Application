package com.qburst.training.bookstoreapplication.Factory;

import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;

/**
 * Payment Processor Interface (Factory Pattern component).
 */
public interface PaymentProcessor {
    PaymentResponse processPayment(PaymentRequest request);
    String getType();
}
