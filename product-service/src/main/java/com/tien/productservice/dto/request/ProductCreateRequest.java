package com.tien.productservice.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    @NotBlank(message = "INVALID_ARGUMENT")
    String sku;

    @NotBlank(message = "INVALID_ARGUMENT")
    String name;

    String description;

    @NotNull(message = "INVALID_ARGUMENT")
    Long categoryId;

    @NotNull(message = "INVALID_ARGUMENT")
    Long brandId;

    @NotNull(message = "INVALID_ARGUMENT")
    @DecimalMin(value = "0.0", inclusive = false, message = "INVALID_ARGUMENT")
    BigDecimal price;

    Boolean active;
}
