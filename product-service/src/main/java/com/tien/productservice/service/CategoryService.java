package com.tien.productservice.service;

import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.entity.Category;
import com.tien.productservice.exception.AppException;
import com.tien.productservice.exception.ErrorCode;
import com.tien.productservice.mapper.CategoryMapper;
import com.tien.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.DUPLICATED, "Category name existed");
        Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Category not found"));
        if (request.getName() != null) category.setName(request.getName());
        if (request.getSlug() != null) category.setSlug(request.getSlug());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse get(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Category not found"));
        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}