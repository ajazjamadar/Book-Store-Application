package com.qburst.training.bookstoreapplication.Dto;

public class PaymentRequest {

    private String bookName;
    private Double amount;
    private String paymentType;

    // Card payment fields
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    // UPI payment fields
    private String upiId;

    public PaymentRequest() {}

    public PaymentRequest(String bookName, Double amount, String paymentType) {
        this.bookName = bookName;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}
