package com.tien.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StockAdjustRequest {
    @NotNull
    Long warehouseId;
    @NotNull
    Long productId;
    @Min(1)
    Integer quantity;
    @NotNull
    Type type;
    String reason;
    public enum Type {
        IN,
        OUT
    }
}