package com.tien.productservice.mapper;

import org.mapstruct.Mapper;

import com.tien.productservice.dto.request.BrandRequest;
import com.tien.productservice.dto.response.BrandResponse;
import com.tien.productservice.entity.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest request);

    BrandResponse toBrandResponse(Brand brand);
}
