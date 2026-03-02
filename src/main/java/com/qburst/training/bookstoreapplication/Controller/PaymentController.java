package com.qburst.training.bookstoreapplication.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qburst.training.bookstoreapplication.Dto.CardPaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;
import com.qburst.training.bookstoreapplication.Dto.UpiPaymentRequest;
import com.qburst.training.bookstoreapplication.Service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final BookService bookService;

    public PaymentController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Process a card payment (credit or debit).
     * paymentType in the body must be "credit" or "debit".
     */
    @PostMapping("/card")
    public PaymentResponse processCardPayment(@Valid @RequestBody CardPaymentRequest paymentRequest) {
        return bookService.processPaymentWithOrder(paymentRequest);
    }

    /**
     * Process a UPI payment.
     * paymentType in the body must be "upi".
     */
    @PostMapping("/upi")
    public PaymentResponse processUpiPayment(@Valid @RequestBody UpiPaymentRequest paymentRequest) {
        return bookService.processPaymentWithOrder(paymentRequest);
    }
}
