package com.tien.productservice.mapper;

import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest request);
    CategoryResponse toResponse(Category category);
}