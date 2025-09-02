package com.tien.paymentservice.dto.response;

import com.tien.paymentservice.entity.PaymentMethod;
import com.tien.paymentservice.entity.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    Long id;
    Long orderId;
    String userId;
    PaymentMethod method;
    BigDecimal amount;
    PaymentStatus status;
    String externalAuthId;
    String externalCaptureId;
    String externalRefundId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}