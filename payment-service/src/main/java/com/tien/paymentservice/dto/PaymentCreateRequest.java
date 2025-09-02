package com.tien.paymentservice.dto;

import com.tien.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreateRequest {
    @NotNull
    Long orderId;
    String customerId; // optional for future
    PaymentMethod method;
    @NotNull
    @DecimalMin(value="0.0", inclusive=true) BigDecimal amount;
}
