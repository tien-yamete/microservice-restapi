package com.tien.productservice.controller;

import java.math.BigDecimal;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tien.productservice.dto.ApiResponse;
import com.tien.productservice.dto.request.ProductCreateRequest;
import com.tien.productservice.dto.request.ProductUpdateRequest;
import com.tien.productservice.dto.response.ProductResponse;
import com.tien.productservice.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse result = productService.create(request);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder().result(result).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse result = productService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable Long id) {
        ProductResponse result = productService.get(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder().result(result).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Page<ProductResponse> result =
                productService.search(q, categoryId, brandId, minPrice, maxPrice, active, page, size, sortBy, sortDir);
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponse>>builder().result(result).build());
    }
}
