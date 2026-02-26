package com.qburst.training.bookstoreapplication.Factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;

/**
 * Credit Card Payment Processor (Factory Pattern component).
 * Validates using Luhn algorithm.
 */
@Component
public class CreditCardProcessor implements PaymentProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(CreditCardProcessor.class);
    
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
            return new PaymentResponse(false, "Card number is required");
        }
        if (!validateCardNumber(request.getCardNumber())) {
            return new PaymentResponse(false, "Invalid card number");
        }
        if (request.getCvv() == null || !validateCVV(request.getCvv())) {
            return new PaymentResponse(false, "Invalid CVV");
        }
        if (request.getExpiryDate() == null || !validateExpiry(request.getExpiryDate())) {
            return new PaymentResponse(false, "Card expired or invalid expiry date");
        }
        if (request.getCardHolderName() == null || request.getCardHolderName().isEmpty()) {
            return new PaymentResponse(false, "Card holder name is required");
        }
        
        String transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8);
        String maskedCard = maskCardNumber(request.getCardNumber());
        String message = "Credit card payment of $%.2f processed successfully for '%s'. Card: %s".formatted(
                request.getAmount(), request.getBookName(), maskedCard);
        
        logger.info("Payment SUCCESS - Transaction: {}", transactionId);
        return new PaymentResponse(true, message, transactionId, request.getAmount());
    }
    
    @Override
    public String getType() {
        return "credit";
    }
    
    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        return luhnCheck(cardNumber.replaceAll("\\s", ""));
    }
    
    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    
    private boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }
    
    private boolean validateExpiry(String expiry) {
        return expiry != null && expiry.matches("(0[1-9]|1[0-2])\\/([0-9]{2})");
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }
}
