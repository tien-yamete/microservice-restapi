package com.tien.orderservice.controller;

import com.tien.orderservice.dto.ApiResponse;
import com.tien.orderservice.dto.request.OrderCreateRequest;
import com.tien.orderservice.dto.response.OrderResponse;
import com.tien.orderservice.dto.request.OrderSearchRequest;
import com.tien.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService service;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderCreateRequest req) {
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder().result(service.create(req)).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder().result(service.get(id)).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> search(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) com.tien.orderservice.entity.OrderStatus status,
            @RequestParam(required = false) java.math.BigDecimal minAmount,
            @RequestParam(required = false) java.math.BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        OrderSearchRequest p = OrderSearchRequest.builder()
                .customerId(customerId).status(status).minAmount(minAmount).maxAmount(maxAmount)
                .page(page).size(size).sortBy(sortBy).sortDir(sortDir).build();
        return ResponseEntity.ok(ApiResponse.<Page<OrderResponse>>builder().result(service.search(p)).build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Cancelled").build());
    }
}