package com.qburst.training.bookstoreapplication.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.qburst.training.bookstoreapplication.Dto.ApiResponse;

/**
 * Centralises error handling so every endpoint returns a consistent JSON structure.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // silently return 404 for missing static resources (e.g. favicon.ico)
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException ex) {
        return new ApiResponse<>(false, ex.getMessage());
    }

    // 405 — wrong HTTP method for the endpoint
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.warn("Method not allowed: {}", ex.getMessage());
        return new ApiResponse<>(false, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ApiResponse<>(false, ex.getMessage());
    }

    // returns a list of field-level errors so the client knows what to fix
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());
        logger.warn("Validation failed: {}", errors);
        return new ApiResponse<>(false, "Validation failed", errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Bad request: {}", ex.getMessage());
        return new ApiResponse<>(false, ex.getMessage());
    }

    // catch-all — log the full trace internally, return a safe message to the client
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ApiResponse<>(false, "An unexpected error occurred. Please try again later.");
    }
}
