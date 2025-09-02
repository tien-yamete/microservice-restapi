package com.tien.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments", indexes = @Index(name="idx_order_id", columnList="orderId", unique = true))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long orderId;
    String userId;

    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    String externalAuthId;
    String externalCaptureId;
    String externalRefundId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist void pre(){
        createdAt = updatedAt = LocalDateTime.now();
        if (status==null)
            status=PaymentStatus.CREATED;
    }
    @PreUpdate void upd(){
        updatedAt = LocalDateTime.now();
    }
}