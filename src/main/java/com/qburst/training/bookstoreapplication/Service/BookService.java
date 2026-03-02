package com.qburst.training.bookstoreapplication.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qburst.training.bookstoreapplication.Config.AppConfiguration;
import com.qburst.training.bookstoreapplication.Dto.BookRequestDto;
import com.qburst.training.bookstoreapplication.Dto.BookResponseDto;
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
 * Service layer for book management, order creation, and payment processing.
 */
@Service
@Transactional(readOnly = true)
public class BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    private final BookRepository bookRepository;
    private final OrderService orderService;
    private final PaymentProcessorFactory paymentFactory;
    private final AppConfiguration appConfiguration;

    public BookService(BookRepository bookRepository, 
                      OrderService orderService,
                      PaymentProcessorFactory paymentFactory,
                      AppConfiguration appConfiguration) {
        this.bookRepository = bookRepository;
        this.orderService = orderService;
        this.paymentFactory = paymentFactory;
        this.appConfiguration = appConfiguration;
    }


    
    // ----------------------------------------------------------------
    // Mapping helpers
    // ----------------------------------------------------------------

    private BookResponseDto toDto(Book book) {
        return new BookResponseDto(book.getId(), book.getName(), book.getAuthor(), book.getPrice());
    }

    private Book toEntity(BookRequestDto dto) {
        return new Book(dto.getName(), dto.getAuthor(), dto.getPrice());
    }

    // ----------------------------------------------------------------
    // CRUD operations
    // ----------------------------------------------------------------

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        Book saved = bookRepository.save(toEntity(dto));
        return toDto(saved);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return toDto(book);
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        return toDto(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.deleteById(id);
    }

    public List<BookResponseDto> searchBooks(String keyword) {
        return bookRepository.searchByKeyword(keyword)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public String orderBook(String bookName) {
        return "Order placed for book: " + bookName;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse processPaymentWithOrder(PaymentRequest paymentRequest) {
        Book book = bookRepository.findByNameIgnoreCase(paymentRequest.getBookName())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "name", paymentRequest.getBookName()));
        
        if (paymentRequest.getAmount() == null) {
            paymentRequest.setAmount(book.getPrice());
        }
        
        Order order = orderService.createOrder(book);
        logger.info("Order #{} created (PENDING) for book '{}'", order.getId(), book.getName());
        
        try {
            PaymentProcessor processor = paymentFactory.getProcessor(paymentRequest.getPaymentType());
            PaymentResponse result = processor.processPayment(paymentRequest);

            // Stamp order ID and payment method onto response
            result.setOrderId(order.getId());
            result.setPaymentMethod(paymentRequest.getPaymentType().toUpperCase());

            if (result.isSuccess()) {
                orderService.updatePaymentStatus(order.getId(), PaymentStatus.SUCCESS);
                logger.info("Order #{} - SUCCESS", order.getId());
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
