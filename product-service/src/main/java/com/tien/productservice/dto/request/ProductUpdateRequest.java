package com.tien.productservice.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String description;
    Long categoryId;
    Long brandId;

    @DecimalMin(value = "0.0", inclusive = false, message = "INVALID_ARGUMENT")
    BigDecimal price;

    Boolean active;
}
