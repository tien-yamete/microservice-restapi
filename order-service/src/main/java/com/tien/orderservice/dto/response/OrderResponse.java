package com.tien.orderservice.dto.response;

import com.tien.orderservice.entity.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String customerId;
    BigDecimal amount;
    OrderStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<OrderItemResponse> items;
}