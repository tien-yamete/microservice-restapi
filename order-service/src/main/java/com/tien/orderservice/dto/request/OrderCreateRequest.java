package com.tien.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    @NotNull String customerId;
    @NotNull BigDecimal amount;
    @NotNull List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {
        @NotNull
        Long productId; 
        @Min(1)
        Integer quantity;
    }
}