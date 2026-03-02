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
import com.qburst.training.bookstoreapplication.Dto.BookRequestDto;
import com.qburst.training.bookstoreapplication.Dto.BookResponseDto;
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
    public ApiResponse<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto created = bookService.createBook(bookRequestDto);
        return new ApiResponse<>(true, "Book added successfully", created);
    }

    // READ - Get all books
    @GetMapping
    public ApiResponse<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> books = bookService.getAllBooks();
        return new ApiResponse<>(true, "Books fetched successfully", books);
    }

    // READ - Get book by ID
    @GetMapping("/{id}")
    public ApiResponse<BookResponseDto> getBookById(@PathVariable Long id) {
        BookResponseDto book = bookService.getBookById(id);
        return new ApiResponse<>(true, "Book found", book);
    }

    // UPDATE - Update a book
    @PutMapping("/{id}")
    public ApiResponse<BookResponseDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto updated = bookService.updateBook(id, bookRequestDto);
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
    public ApiResponse<List<BookResponseDto>> searchBooks(@RequestParam String keyword) {
        List<BookResponseDto> books = bookService.searchBooks(keyword);
        return new ApiResponse<>(true, "Search results", books);
    }

    // Order a book (simple, no payment)
    @PostMapping("/order")
    public String orderBook(@RequestParam String bookName){
        return bookService.orderBook(bookName);
    }
}
