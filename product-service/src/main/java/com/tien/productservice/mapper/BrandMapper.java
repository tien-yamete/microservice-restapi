package com.tien.productservice.mapper;

import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toEntity(BrandRequest request);
    BrandResponse toResponse(Brand brand);
}