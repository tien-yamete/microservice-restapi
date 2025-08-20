package com.tien.productservice.service;

import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.entity.Brand;
import com.tien.productservice.exception.AppException;
import com.tien.productservice.exception.ErrorCode;
import com.tien.productservice.mapper.BrandMapper;
import com.tien.productservice.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public BrandResponse create(BrandRequest request) {
        if (brandRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.DUPLICATED, "Brand name existed");
        Brand brand = brandMapper.toEntity(request);
        brandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public BrandResponse update(Long id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Brand not found"));
        if (request.getName() != null) brand.setName(request.getName());
        if (request.getDescription() != null) brand.setDescription(request.getDescription());
        brandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }

    public BrandResponse get(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Brand not found"));
        return brandMapper.toResponse(brand);
    }

    public List<BrandResponse> list() {
        return brandRepository.findAll().stream().map(brandMapper::toResponse).toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }
}