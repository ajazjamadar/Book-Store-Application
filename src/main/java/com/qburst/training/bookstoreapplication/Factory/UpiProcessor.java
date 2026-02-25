package com.qburst.training.bookstoreapplication.Factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;

/**
 * UPI Payment Processor (Factory Pattern component).
 * Format: username@bankname (e.g., user@paytm)
 */
@Component
public class UpiProcessor implements PaymentProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(UpiProcessor.class);
    
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (request.getUpiId() == null || request.getUpiId().isEmpty()) {
            return new PaymentResponse(false, "UPI ID is required");
        }
        if (!validateUpiId(request.getUpiId())) {
            return new PaymentResponse(false, "Invalid UPI ID format. Use: username@bankname");
        }
        
        String transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 8);
        String message = String.format("UPI payment of $%.2f processed successfully for '%s'. UPI ID: %s",
                request.getAmount(), request.getBookName(), maskUpiId(request.getUpiId()));
        
        logger.info("Payment SUCCESS - Transaction: {}", transactionId);
        return new PaymentResponse(true, message, transactionId, request.getAmount());
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
        if (upiId == null || !upiId.contains("@")) {
            return "***@***";
        }
        String[] parts = upiId.split("@");
        String username = parts[0];
        if (username.length() <= 2) {
            return "***@" + parts[1];
        }
        String masked = username.charAt(0) + "***" + username.charAt(username.length() - 1);
        return masked + "@" + parts[1];
    }
}
