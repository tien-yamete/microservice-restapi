package com.tien.orderservice.dto.request;

import com.tien.orderservice.entity.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSearchRequest {
    String customerId;
    OrderStatus status;
    BigDecimal minAmount;
    BigDecimal maxAmount;
    Integer page;
    Integer size;
    String sortBy;
    String sortDir;
}