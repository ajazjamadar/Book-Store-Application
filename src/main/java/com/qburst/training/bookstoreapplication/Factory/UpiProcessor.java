package com.qburst.training.bookstoreapplication.Factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;
import com.qburst.training.bookstoreapplication.Dto.UpiPaymentRequest;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;

/**
 * Handles UPI payment processing.
 * Expects UPI ID in the format username@bankname (e.g., user@paytm).
 */
@Component
public class UpiProcessor implements PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UpiProcessor.class);

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (!(request instanceof UpiPaymentRequest upiRequest)) {
            return new PaymentResponse(PaymentStatus.FAILED, "Invalid request type for UPI payment");
        }

        if (upiRequest.getUpiId() == null || upiRequest.getUpiId().isBlank()) {
            return new PaymentResponse(PaymentStatus.FAILED, "UPI ID is required");
        }
        if (!validateUpiId(upiRequest.getUpiId())) {
            return new PaymentResponse(PaymentStatus.FAILED, "Invalid UPI ID format. Use: username@bankname");
        }

        String transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String message = "UPI payment of $%.2f processed successfully for '%s'. UPI ID: %s"
                .formatted(upiRequest.getAmount(), upiRequest.getBookName(), maskUpiId(upiRequest.getUpiId()));

        logger.info("Payment SUCCESS - Transaction: {}", transactionId);
        return new PaymentResponse(PaymentStatus.SUCCESS, message, transactionId, upiRequest.getAmount(), "UPI");
    }

    @Override
    public String getType() {
        return "upi";
    }

    private boolean validateUpiId(String upiId) {
        if (upiId == null) return false;
        return upiId.matches("^[\\w.-]+@[\\w.-]+$");
    }

    private String maskUpiId(String upiId) {
        if (upiId == null || !upiId.contains("@")) return "***@***";
        String[] parts = upiId.split("@");
        String username = parts[0];
        if (username.length() <= 2) return "***@" + parts[1];
        String masked = username.charAt(0) + "***" + username.charAt(username.length() - 1);
        return masked + "@" + parts[1];
    }
}
