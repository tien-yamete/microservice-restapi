package com.tien.productservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tien.productservice.dto.ApiResponse;
import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.createCategory(request);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Long id) {
        CategoryResponse result = categoryService.getCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder().result(result).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategory() {
        List<CategoryResponse> result = categoryService.getAllCategory();
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder().result(result).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }
}
