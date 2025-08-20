package com.tien.productservice.controller;

import com.tien.productservice.dto.ApiResponse;
import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody BrandRequest request) {
        BrandResponse result = brandService.create(request);
        return ResponseEntity.ok(ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<BrandResponse>> update(@PathVariable Long id, @Valid @RequestBody BrandRequest request) {
        BrandResponse result = brandService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> get(@PathVariable Long id) {
        BrandResponse result = brandService.get(id);
        return ResponseEntity.ok(ApiResponse.<BrandResponse>builder().result(result).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> list() {
        List<BrandResponse> result = brandService.list();
        return ResponseEntity.ok(ApiResponse.<List<BrandResponse>>builder().result(result).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }
}