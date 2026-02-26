package com.qburst.training.bookstoreapplication.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qburst.training.bookstoreapplication.Config.AppConfiguration;
import com.qburst.training.bookstoreapplication.Dto.PaymentRequest;
import com.qburst.training.bookstoreapplication.Dto.PaymentResponse;
import com.qburst.training.bookstoreapplication.Entity.Book;
import com.qburst.training.bookstoreapplication.Entity.Order;
import com.qburst.training.bookstoreapplication.Factory.PaymentProcessor;
import com.qburst.training.bookstoreapplication.Factory.PaymentProcessorFactory;
import com.qburst.training.bookstoreapplication.Repository.BookRepository;
import com.qburst.training.bookstoreapplication.enums.PaymentStatus;
import com.qburst.training.bookstoreapplication.exception.ResourceNotFoundException;

/**
 * Service layer for books, orders, and payment processing.
 * 
 * Design Patterns:
 * - Factory Pattern: PaymentProcessorFactory
 * - Singleton Pattern: AppConfiguration
 */
@Service
public class BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    private final BookRepository bookRepository;
    private final OrderService orderService;
    private final EmailService emailService;
    private final PaymentProcessorFactory paymentFactory;
    private final AppConfiguration appConfiguration;

    public BookService(BookRepository bookRepository, 
                      OrderService orderService,
                      EmailService emailService,
                      PaymentProcessorFactory paymentFactory,
                      AppConfiguration appConfiguration) {
        this.bookRepository = bookRepository;
        this.orderService = orderService;
        this.emailService = emailService;
        this.paymentFactory = paymentFactory;
        this.appConfiguration = appConfiguration;
    }


    
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setName(bookDetails.getName());
        book.setAuthor(bookDetails.getAuthor());
        book.setPrice(bookDetails.getPrice());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        getBookById(id);
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByKeyword(keyword);
    }

    public String orderBook(String bookName) {
        emailService.sendEmail(bookName);
        return "Order placed for book: " + bookName;
    }
    
    /**
     * Processes payment using Factory Pattern to select appropriate processor.
     * Creates order, processes payment, updates status, and sends confirmation.
     */
    public PaymentResponse processPaymentWithOrder(PaymentRequest paymentRequest) {
        Book book = searchBooks(paymentRequest.getBookName()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book", "name", paymentRequest.getBookName()));
        
        if (paymentRequest.getAmount() == null) {
            paymentRequest.setAmount(book.getPrice());
        }
        
        Order order = orderService.createOrder(paymentRequest.getBookName());
        logger.info("Order #{} created (PENDING)", order.getId());
        
        try {
            PaymentProcessor processor = paymentFactory.getProcessor(paymentRequest.getPaymentType());
            PaymentResponse result = processor.processPayment(paymentRequest);
            
            if (result.isSuccess()) {
                orderService.updatePaymentStatus(order.getId(), PaymentStatus.SUCCESS);
                logger.info("Order #{} - SUCCESS", order.getId());
                emailService.sendEmail(paymentRequest.getBookName());
            } else {
                orderService.updatePaymentStatus(order.getId(), PaymentStatus.FAILED);
                logger.warn("Order #{} - FAILED", order.getId());
            }
            
            return result;
            
        } catch (Exception e) {
            orderService.updatePaymentStatus(order.getId(), PaymentStatus.FAILED);
            logger.error("Order #{} - Error: {}", order.getId(), e.getMessage());
            throw e;
        }
    }
    
    public String getApplicationInfo() {
        return "%s v%s - %s".formatted(
                appConfiguration.getName(),
                appConfiguration.getVersion(),
                appConfiguration.getDescription());
    }
}
