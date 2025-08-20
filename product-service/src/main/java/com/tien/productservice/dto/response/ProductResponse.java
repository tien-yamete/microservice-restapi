package com.tien.productservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String sku;
    String name;
    String description;
    Long categoryId;
    String categoryName;
    Long brandId;
    String brandName;
    BigDecimal price;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
