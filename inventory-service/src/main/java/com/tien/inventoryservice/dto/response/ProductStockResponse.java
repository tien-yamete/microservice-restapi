package com.tien.inventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductStockResponse {
    Long id;
    Long productId;
    Long warehouseId;
    Integer available;
    Integer reserved;
}