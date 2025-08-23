package com.tien.inventoryservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WarehouseRequest {
    @NotBlank
    String code;
    @NotBlank
    String name;
    String address;
    String region;
}