package com.tien.paymentservice.controller;

import com.tien.paymentservice.dto.ApiResponse;
import com.tien.paymentservice.dto.PaymentCreateRequest;
import com.tien.paymentservice.dto.PaymentResponse;
import com.tien.paymentservice.entity.Payment;
import com.tien.paymentservice.repository.PaymentRepository;
import com.tien.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;
    PaymentRepository paymentRepository;

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> byOrder(@PathVariable Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElse(null);
        return ResponseEntity.ok(ApiResponse.<PaymentResponse>builder()
                .result(p == null ? null : PaymentResponse.builder()
                        .id(p.getId())
                        .orderId(p.getOrderId())
                        .amount(p.getAmount())
                        .method(p.getMethod())
                        .status(p.getStatus())
                        .build())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@RequestBody @Valid PaymentCreateRequest req){
        Payment p = paymentService.create(req.getOrderId(), req.getCustomerId(), req.getAmount(), req.getMethod());
        return ResponseEntity.ok(ApiResponse.<PaymentResponse>builder()
                .result(PaymentResponse.builder()
                        .id(p.getId())
                        .orderId(p.getOrderId())
                        .amount(p.getAmount())
                        .method(p.getMethod())
                        .status(p.getStatus())
                        .build())
                .build());
    }

    @PostMapping("/{orderId}/authorize")
    public ResponseEntity<ApiResponse<?>> authorize(@PathVariable Long orderId){
        paymentService.authorize(orderId);
        return ResponseEntity.ok(ApiResponse.builder().message("Authorized").build());
    }

    @PostMapping("/{orderId}/capture")
    public ResponseEntity<ApiResponse<?>> capture(@PathVariable Long orderId){
        paymentService.capture(orderId);
        return ResponseEntity.ok(ApiResponse.builder().message("Captured").build());
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponse<?>> refund(@PathVariable Long orderId){
        paymentService.refund(orderId);
        return ResponseEntity.ok(ApiResponse.builder().message("Refunded").build());
    }
}