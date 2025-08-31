package com.tien.inventoryservice.controller;

import com.tien.inventoryservice.dto.ApiResponse;
import com.tien.inventoryservice.dto.request.StockAdjustRequest;
import com.tien.inventoryservice.dto.request.StockMoveRequest;
import com.tien.inventoryservice.dto.response.ProductStockResponse;
import com.tien.inventoryservice.service.StockService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockController {

    StockService stockService;

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<ProductStockResponse>> adjust(@Valid @RequestBody StockAdjustRequest req){
        return ResponseEntity.ok(ApiResponse.<ProductStockResponse>builder().result(stockService.adjust(req)).build());
    }

    @PostMapping("/move")
    public ResponseEntity<ApiResponse<?>> move(@Valid @RequestBody StockMoveRequest req){
        stockService.move(req);
        return ResponseEntity.ok(ApiResponse.builder().message("Moved").build());
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductStockResponse>>> byProduct(@PathVariable Long productId){
        return ResponseEntity.ok(ApiResponse.<List<ProductStockResponse>>builder().result(stockService.listByProduct(productId)).build());
    }
}