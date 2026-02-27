package com.qburst.training.bookstoreapplication.Dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Base class for payment requests.
 * Extended by CardPaymentRequest and UpiPaymentRequest.
 */
public abstract class PaymentRequest {

    @NotBlank(message = "Book name is required")
    private String bookName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment type is required")
    private String paymentType;

    public PaymentRequest() {}

    public PaymentRequest(String bookName, BigDecimal amount, String paymentType) {
        this.bookName = bookName;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
}
