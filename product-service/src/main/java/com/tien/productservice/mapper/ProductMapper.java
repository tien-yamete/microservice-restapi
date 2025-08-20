package com.tien.productservice.mapper;

import com.tien.productservice.dto.request.ProductCreateRequest;
import com.tien.productservice.dto.response.ProductResponse;
import com.tien.productservice.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromCreateRequest(ProductCreateRequest request, @MappingTarget Product product);
}