package com.qburst.training.bookstoreapplication.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qburst.training.bookstoreapplication.Entity.Book;
import com.qburst.training.bookstoreapplication.Entity.Order;
import com.qburst.training.bookstoreapplication.Repository.BookRepository;
import com.qburst.training.bookstoreapplication.Repository.OrderRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public BookService(BookRepository bookRepository, OrderRepository orderRepository, EmailService emailService) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    // CREATE
    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    // READ - Get all books
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    // READ - Get book by ID
    public Book getBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // UPDATE
    public Book updateBook(Long id, Book bookDetails){
        Book book = getBookById(id);
        book.setName(bookDetails.getName());
        book.setAuthor(bookDetails.getAuthor());
        book.setPrice(bookDetails.getPrice());
        return bookRepository.save(book);
    }

    // DELETE
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    // SEARCH - Search books by keyword
    public List<Book> searchBooks(String keyword){
        return bookRepository.searchByKeyword(keyword);
    }

    // Order a book
    public String orderBook(String bookName){
        Order order = new Order(bookName);
        orderRepository.save(order);
        emailService.sendEmail(bookName);
        return "Order placed for book: " + bookName;
    }
}
