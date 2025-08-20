package com.tien.productservice.mapper;

import org.mapstruct.Mapper;

import com.tien.productservice.dto.request.CategoryRequest;
import com.tien.productservice.dto.response.CategoryResponse;
import com.tien.productservice.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);
}
