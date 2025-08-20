package com.tien.productservice.controller;

import com.tien.productservice.dto.ApiResponse;
import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.create(request);
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> get(@PathVariable Long id) {
        CategoryResponse result = categoryService.get(id);
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        List<CategoryResponse> result = categoryService.list();
        return ResponseEntity.ok(ApiResponse.<List<CategoryResponse>>builder().result(result).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }
}