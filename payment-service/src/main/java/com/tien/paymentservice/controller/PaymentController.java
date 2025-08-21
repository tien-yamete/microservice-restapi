package com.tien.paymentservice.controller;

import com.tien.paymentservice.dto.ApiResponse;
import com.tien.paymentservice.dto.PaymentResponse;
import com.tien.paymentservice.entity.Payment;
import com.tien.paymentservice.repository.PaymentRepository;
import com.tien.paymentservice.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentRepository paymentRepository;
    PaymentService paymentService;

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> byOrder(@PathVariable Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElse(null);
        if (p == null) return ResponseEntity.ok(ApiResponse.<PaymentResponse>builder().message("Not found").build());
        PaymentResponse resp = PaymentResponse.builder()
                .id(p.getId()).orderId(p.getOrderId()).customerId(p.getCustomerId()).method(p.getMethod()).amount(p.getAmount()).status(p.getStatus())
                .externalAuthId(p.getExternalAuthId()).externalCaptureId(p.getExternalCaptureId()).externalRefundId(p.getExternalRefundId())
                .createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt()).build();
        return ResponseEntity.ok(ApiResponse.<PaymentResponse>builder().result(resp).build());
    }

    // Manual endpoints (optional): authorize/capture/refund by order
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