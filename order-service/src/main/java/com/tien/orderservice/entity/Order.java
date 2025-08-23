package com.tien.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String customerId;
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<OrderItem> items = new ArrayList<>();

    @PrePersist void pre() {
        createdAt = updatedAt = LocalDateTime.now();
        if (status==null) status = OrderStatus.PENDING;
    }
    @PreUpdate void upd() {
        updatedAt = LocalDateTime.now();
    }
}