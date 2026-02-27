package com.qburst.training.bookstoreapplication.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for UPI payment requests.
 * UPI ID format: username@bankname (e.g., user@paytm, alice@upi)
 */
public class UpiPaymentRequest extends PaymentRequest {

    @NotBlank(message = "UPI ID is required")
    @Pattern(
        regexp = "^[\\w.-]+@[\\w.-]+$",
        message = "Invalid UPI ID format. Use: username@bankname (e.g., user@paytm)"
    )
    private String upiId;

    public UpiPaymentRequest() {}

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}
