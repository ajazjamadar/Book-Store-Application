package com.qburst.training.bookstoreapplication.Dto;

public class PaymentResponse {

    private boolean success;
    private String message;
    private String transactionId;
    private Double amount;

    public PaymentResponse() {}

    public PaymentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PaymentResponse(boolean success, String message, String transactionId, Double amount) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
