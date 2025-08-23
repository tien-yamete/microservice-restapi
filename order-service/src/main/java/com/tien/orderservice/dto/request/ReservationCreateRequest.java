
package com.tien.orderservice.dto.request;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationCreateRequest {
    Long orderId;
    Long warehouseId;
    List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {
        Long productId;
        Integer quantity;
    }
}
