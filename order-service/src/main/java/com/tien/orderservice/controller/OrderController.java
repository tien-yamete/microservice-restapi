package com.tien.orderservice.controller;

import com.tien.orderservice.dto.ApiResponse;
import com.tien.orderservice.dto.request.OrderCreateRequest;
import com.tien.orderservice.dto.response.OrderResponse;
import com.tien.orderservice.dto.request.OrderSearchRequest;
import com.tien.orderservice.entity.OrderStatus;
import com.tien.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService service;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody @Valid OrderCreateRequest req){
        var result = service.create(req);
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> get(@PathVariable Long id){
        var result = service.get(id);
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder().result(result).build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> search(@RequestBody OrderSearchRequest p){
        var result = service.search(p);
        return ResponseEntity.ok(ApiResponse.<Page<OrderResponse>>builder().result(result).build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(@PathVariable Long id,
                                                                   @RequestParam OrderStatus status){
        var response = service.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder().result(response).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelByDelete(@PathVariable Long id){
        service.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().message("Order cancelled").build());
    }
}