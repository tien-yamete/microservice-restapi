package com.tien.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationCreateRequest {
    @NotNull Long orderId;
    @NotNull Long warehouseId;
    @NotNull List<Item> items;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item { @NotNull Long productId; @Min(1) Integer quantity; }
}