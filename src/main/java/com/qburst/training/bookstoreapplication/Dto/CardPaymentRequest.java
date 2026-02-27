package com.qburst.training.bookstoreapplication.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body for card payments (credit and debit).
 * CVV is accepted on input but never included in any response.
 */
public class CardPaymentRequest extends PaymentRequest {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{13,19}", message = "Card number must be 13-19 digits (no spaces)")
    private String cardNumber;

    // write-only: excluded from serialization, cleared after validation
    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "\\d{3,4}", message = "CVV must be 3 or 4 digits")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cvv;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "(0[1-9]|1[0-2])/([0-9]{2})", message = "Expiry date must be MM/YY")
    private String expiryDate;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    public CardPaymentRequest() {}

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    // null out the CVV once validation is done so it doesn't hang around
    public void clearSensitiveData() {
        this.cvv = null;
    }
}
