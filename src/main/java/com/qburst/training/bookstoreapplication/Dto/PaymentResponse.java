package com.qburst.training.bookstoreapplication.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.qburst.training.bookstoreapplication.enums.PaymentStatus;

public class PaymentResponse {

    private PaymentStatus status;
    private String message;
    private String transactionId;
    private BigDecimal amount;
    private String paymentMethod;
    private Long orderId;
    private LocalDateTime timestamp;

    public PaymentResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /** Used for failure responses (no transaction details). */
    public PaymentResponse(PaymentStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /** Used for successful responses (full details). */
    public PaymentResponse(PaymentStatus status, String message, String transactionId,
                           BigDecimal amount, String paymentMethod) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.timestamp = LocalDateTime.now();
    }

    /** Convenience check used by service layer. */
    public boolean isSuccess() {
        return PaymentStatus.SUCCESS == this.status;
    }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
