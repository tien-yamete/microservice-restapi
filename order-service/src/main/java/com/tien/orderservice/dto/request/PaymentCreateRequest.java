package com.tien.orderservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreateRequest {
    Long orderId;
    String customerId; // optional
    String method;     // keep as String to avoid coupling enums
    BigDecimal amount;
}
