package com.tien.productservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    ProductMapper productMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ProductResponse create(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new AppException(ErrorCode.DUPLICATED);
        }
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Brand brand = brandRepository
                .findById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Product  product = productMapper.toProduct(request);

        product.setCategory(category);
        product.setBrand(brand);

        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (request.getCategoryId() != null) {
            Category category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
            product.setCategory(category);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository
                    .findById(request.getBrandId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
            product.setBrand(brand);
        }

        productMapper.updateProductFromCreateRequest(request, product);

        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse get(Long id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void delete(Long id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        productRepository.delete(product);
    }

    public Page<ProductResponse> search(
            String keyword,
            Long categoryId,
            Long brandId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(ProductSpecifications.hasKeyword(keyword))
                .and(ProductSpecifications.hasCategoryId(categoryId))
                .and(ProductSpecifications.hasBrandId(brandId))
                .and(ProductSpecifications.priceGte(minPrice))
                .and(ProductSpecifications.priceLte(maxPrice))
                .and(ProductSpecifications.isActive(active));

        Sort sort = Sort.by(
                (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC,
                (sortBy != null) ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 100), sort);

        return productRepository.findAll(spec, pageable).map(productMapper::toProductResponse);
    }

    @Transactional
    public List<ProductResponse> findAll() {
        // sắp xếp mới nhất trước cho đồng nhất với search mặc định
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
