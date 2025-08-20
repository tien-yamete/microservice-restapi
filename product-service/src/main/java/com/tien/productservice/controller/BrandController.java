package com.tien.productservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tien.productservice.dto.ApiResponse;
import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.service.BrandService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@Valid @RequestBody BrandRequest request) {
        BrandResponse result = brandService.create(request);
        return ResponseEntity.ok(
                ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable Long id, @Valid @RequestBody BrandRequest request) {
        BrandResponse result = brandService.updateBrand(id, request);
        return ResponseEntity.ok(
                ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrand(@PathVariable Long id) {
        BrandResponse result = brandService.getBrand(id);
        return ResponseEntity.ok(
                ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {
        List<BrandResponse> result = brandService.getAllBrands();
        return ResponseEntity.ok(
                ApiResponse.<List<BrandResponse>>builder().result(result).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }
}
