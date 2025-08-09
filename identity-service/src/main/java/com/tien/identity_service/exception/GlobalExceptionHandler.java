package com.tien.identity_service.exception;

import com.tien.identity_service.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumkey = e.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumkey);
        }catch (IllegalArgumentException iae){

        }


        ApiResponse response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
