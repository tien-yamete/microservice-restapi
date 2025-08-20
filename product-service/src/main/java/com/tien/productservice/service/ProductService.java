package com.tien.productservice.service;

import com.tien.productservice.dto.request.ProductCreateRequest;
import com.tien.productservice.dto.request.ProductUpdateRequest;
import com.tien.productservice.dto.response.ProductResponse;
import com.tien.productservice.entity.Brand;
import com.tien.productservice.entity.Category;
import com.tien.productservice.entity.Product;
import com.tien.productservice.exception.AppException;
import com.tien.productservice.exception.ErrorCode;
import com.tien.productservice.mapper.ProductMapper;
import com.tien.productservice.repository.BrandRepository;
import com.tien.productservice.repository.CategoryRepository;
import com.tien.productservice.repository.ProductRepository;
import com.tien.productservice.repository.ProductSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    ProductMapper productMapper;

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ProductResponse create(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new AppException(ErrorCode.DUPLICATED, "SKU already exists");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Category not found"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Brand not found"));

        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .brand(brand)
                .price(request.getPrice())
                .active(Optional.ofNullable(request.getActive()).orElse(Boolean.TRUE))
                .build();
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Product not found"));

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Category not found"));
            product.setCategory(category);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Brand not found"));
            product.setBrand(brand);
        }
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getActive() != null) product.setActive(request.getActive());

        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Product not found"));
        return productMapper.toResponse(product);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Product not found"));
        productRepository.delete(product);
    }

    public Page<ProductResponse> search(String keyword, Long categoryId, Long brandId,
                                       BigDecimal minPrice, BigDecimal maxPrice, Boolean active,
                                       int page, int size, String sortBy, String sortDir) {
        Specification<Product> spec = Specification.where(ProductSpecifications.hasKeyword(keyword))
                .and(ProductSpecifications.hasCategoryId(categoryId))
                .and(ProductSpecifications.hasBrandId(brandId))
                .and(ProductSpecifications.priceGte(minPrice))
                .and(ProductSpecifications.priceLte(maxPrice))
                .and(ProductSpecifications.isActive(active));

        Sort sort = Sort.by((sortDir != null && sortDir.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC,
                (sortBy != null) ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.min(size,100), sort);

        return productRepository.findAll(spec, pageable).map(productMapper::toResponse);
    }
}