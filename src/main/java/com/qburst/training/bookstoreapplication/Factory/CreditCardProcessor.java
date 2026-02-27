package com.qburst.training.bookstoreapplication.Factory;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qburst.training.bookstoreapplication.Dto.CardPaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;

/**
 * Handles credit card payment processing.
 * Validates card number length, expiry date, and CVV before proceeding.
 */
@Component
public class CreditCardProcessor implements PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CreditCardProcessor.class);

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (!(request instanceof CardPaymentRequest cardRequest)) {
            return new PaymentResponse(PaymentStatus.FAILED, "Invalid request type for credit card payment");
        }

        // validate card fields
        if (cardRequest.getCardNumber() == null || cardRequest.getCardNumber().isEmpty()) {
            return new PaymentResponse(PaymentStatus.FAILED, "Card number is required");
        }
        if (!isValidCardLength(cardRequest.getCardNumber())) {
            return new PaymentResponse(PaymentStatus.FAILED, "Invalid card number");
        }
        if (cardRequest.getCvv() == null || !validateCVV(cardRequest.getCvv())) {
            return new PaymentResponse(PaymentStatus.FAILED, "Invalid CVV");
        }
        if (cardRequest.getExpiryDate() == null || !validateExpiry(cardRequest.getExpiryDate())) {
            return new PaymentResponse(PaymentStatus.FAILED, "Card expired or invalid expiry date");
        }
        if (cardRequest.getCardHolderName() == null || cardRequest.getCardHolderName().isBlank()) {
            return new PaymentResponse(PaymentStatus.FAILED, "Card holder name is required");
        }

        // CVV is no longer needed after this point
        cardRequest.clearSensitiveData();

        String maskedCard    = maskCardNumber(cardRequest.getCardNumber());
        String transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String message = "Credit card payment of $%.2f processed successfully for '%s'. Card: %s"
                .formatted(cardRequest.getAmount(), cardRequest.getBookName(), maskedCard);

        logger.info("Payment SUCCESS - Transaction: {}", transactionId);
        return new PaymentResponse(PaymentStatus.SUCCESS, message, transactionId, cardRequest.getAmount(), "CREDIT_CARD");
    }

    @Override
    public String getType() {
        return "credit";
    }

    private boolean isValidCardLength(String cardNumber) {
        String digits = cardNumber.replaceAll("\\s", "");
        return digits.length() >= 13 && digits.length() <= 19;
    }

    private boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }

    private boolean validateExpiry(String expiry) {
        if (expiry == null || !expiry.matches("(0[1-9]|1[0-2])/([0-9]{2})")) return false;
        try {
            YearMonth cardExpiry = YearMonth.parse(expiry, DateTimeFormatter.ofPattern("MM/yy"));
            return !cardExpiry.isBefore(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String maskCardNumber(String cardNumber) {
        String digits = cardNumber.replaceAll("\\s", "");
        if (digits.length() < 4) return "****";
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }
}
