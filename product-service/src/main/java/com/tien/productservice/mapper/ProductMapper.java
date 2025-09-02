package com.tien.productservice.mapper;

import com.tien.productservice.dto.request.ProductUpdateRequest;
import org.mapstruct.*;

import com.tien.productservice.dto.request.ProductCreateRequest;
import com.tien.productservice.dto.response.ProductResponse;
import com.tien.productservice.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreateRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    ProductResponse toProductResponse(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromCreateRequest(ProductUpdateRequest request, @MappingTarget Product product);
}
