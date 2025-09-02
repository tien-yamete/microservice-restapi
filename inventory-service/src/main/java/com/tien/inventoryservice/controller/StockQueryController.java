package com.tien.inventoryservice.controller;

import com.tien.inventoryservice.dto.ApiResponse;
import com.tien.inventoryservice.dto.PageResponse;
import com.tien.inventoryservice.dto.response.ProductStockResponse;
import com.tien.inventoryservice.entity.ProductStock;
import com.tien.inventoryservice.entity.StockTransaction;
import com.tien.inventoryservice.mapper.ProductStockMapper;
import com.tien.inventoryservice.repository.ProductStockRepository;
import com.tien.inventoryservice.repository.StockTransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockQueryController {

    ProductStockRepository productStockRepository;
    StockTransactionRepository stockTransactionRepository;
    ProductStockMapper productStockMapper;

    /**
     * GET /inventory/stock?productId=&warehouseId=&page=&size=
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductStockResponse>>> list(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var pageable = PageRequest.of(page, size);
        Page<ProductStock> pg;
        if (productId != null && warehouseId != null) {
            pg = productStockRepository.findByProductIdAndWarehouse_Id(productId, warehouseId, pageable);
        } else if (productId != null) {
            pg = productStockRepository.findByProductId(productId, pageable);
        } else if (warehouseId != null) {
            pg = productStockRepository.findByWarehouse_Id(warehouseId, pageable);
        } else {
            pg = productStockRepository.findAll(pageable);
        }

        var items = pg.getContent().stream()
                .map(productStockMapper::toPeProductStock)
                .collect(Collectors.toList());

        var result = PageResponse.<ProductStockResponse>builder()
                .page(pg.getNumber())
                .size(pg.getSize())
                .total(pg.getTotalElements())
                .items(items)
                .build();

        return ResponseEntity.ok(ApiResponse.<PageResponse<ProductStockResponse>>builder()
                .result(result).build());
    }

    /**
     * GET /inventory/stock/ledger?productId=&warehouseId=&page=&size=
     */
    @GetMapping("/ledger")
    public ResponseEntity<ApiResponse<Page<StockTransaction>>> ledger(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        var pageable = PageRequest.of(page, size);
        var ledger = stockTransactionRepository.findLedger(productId, warehouseId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<StockTransaction>>builder().result(ledger).build());
    }
}
