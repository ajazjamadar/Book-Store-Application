package com.qburst.training.bookstoreapplication.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qburst.training.bookstoreapplication.Dto.ApiResponse;
import com.qburst.training.bookstoreapplication.Dto.CardPaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;
import com.qburst.training.bookstoreapplication.Dto.UpiPaymentRequest;
import com.qburst.training.bookstoreapplication.Entity.Book;
import com.qburst.training.bookstoreapplication.Service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // CREATE - Add a new book
    @PostMapping
    public ApiResponse<Book> createBook(@RequestBody Book book) {
        Book created = bookService.createBook(book);
        return new ApiResponse<>(true, "Book added successfully", created);
    }

    // READ - Get all books
    @GetMapping
    public ApiResponse<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ApiResponse<>(true, "Books fetched successfully", books);
    }

    // READ - Get book by ID
    @GetMapping("/{id}")
    public ApiResponse<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return new ApiResponse<>(true, "Book found", book);
    }

    // UPDATE - Update a book
    @PutMapping("/{id}")
    public ApiResponse<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updated = bookService.updateBook(id, book);
        return new ApiResponse<>(true, "Book updated successfully", updated);
    }

    // DELETE - Delete a book
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ApiResponse<>(true, "Book deleted successfully");
    }

    // SEARCH - Search books by keyword
    @GetMapping("/search")
    public ApiResponse<List<Book>> searchBooks(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooks(keyword);
        return new ApiResponse<>(true, "Search results", books);
    }

    /**
     * Process a card payment (credit or debit).
     * paymentType in the body must be "credit" or "debit".
     */
    @PostMapping("/process-payment/card")
    public PaymentResponse processCardPayment(@Valid @RequestBody CardPaymentRequest paymentRequest) {
        return bookService.processPaymentWithOrder(paymentRequest);
    }

    /**
     * Process a UPI payment.
     * paymentType in the body must be "upi".
     */
    @PostMapping("/process-payment/upi")
    public PaymentResponse processUpiPayment(@Valid @RequestBody UpiPaymentRequest paymentRequest) {
        return bookService.processPaymentWithOrder(paymentRequest);
    }

    // Order a book (simple, no payment)
    @PostMapping("/order")
    public String orderBook(@RequestParam String bookName){
        return bookService.orderBook(bookName);
    }

}
