package com.tien.inventoryservice.exception;

// GlobalExceptionHandler: Chịu trách nhiệm xử lý tập trung tất cả exception trong hệ thống.

import com.tien.inventoryservice.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<?>> handleAny(Exception e) {
        log.error("Uncategorized", e);
        var ec = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(ec.getStatusCode())
                .body(ApiResponse.builder().code(ec.getCode()).message(ec.getMessage()).build());
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<?>> handleApp(AppException e) {
        var ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatusCode())
                .body(ApiResponse.builder().code(ec.getCode()).message(e.getMessage()).build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handleDenied(AccessDeniedException e) {
        var ec = ErrorCode.INVALID_ARGUMENT;
        return ResponseEntity.status(ec.getStatusCode())
                .body(ApiResponse.builder().code(ec.getCode()).message("Access denied").build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        var ec = ErrorCode.INVALID_ARGUMENT;
        String msg = e.getFieldError()!=null ? e.getFieldError().getDefaultMessage() : ec.getMessage();
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(ec.getCode()).message(msg).build());
    }
}
