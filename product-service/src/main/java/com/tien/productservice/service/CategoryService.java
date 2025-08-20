package com.tien.productservice.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.entity.Category;
import com.tien.productservice.exception.AppException;
import com.tien.productservice.exception.ErrorCode;
import com.tien.productservice.mapper.CategoryMapper;
import com.tien.productservice.repository.CategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) throw new AppException(ErrorCode.DUPLICATED);
        Category category = categoryMapper.toCategory(request);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (request.getName() != null) category.setName(request.getName());
        if (request.getSlug() != null) category.setSlug(request.getSlug());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse getCategory(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
