package com.tien.productservice.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.entity.Brand;
import com.tien.productservice.exception.AppException;
import com.tien.productservice.exception.ErrorCode;
import com.tien.productservice.mapper.BrandMapper;
import com.tien.productservice.repository.BrandRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public BrandResponse create(BrandRequest request) {
        if (brandRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.DUPLICATED);
        Brand brand = brandMapper.toBrand(request);
        brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (request.getName() != null)
            brand.setName(request.getName());
        if (request.getDescription() != null)
            brand.setDescription(request.getDescription());
        brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    public BrandResponse getBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return brandMapper.toBrandResponse(brand);
    }

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toBrandResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PRODUCT_WRITE')")
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
