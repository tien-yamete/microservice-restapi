package com.tien.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="stock_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Type type;

    Long productId;
    Long warehouseFromId;
    Long warehouseToId;
    Integer quantity;
    String reason;
    LocalDateTime createdAt;

    public enum Type {
        ADJUST_IN, ADJUST_OUT, MOVE, RESERVE, RELEASE, COMMIT
    }

    @PrePersist void pre(){
        createdAt = LocalDateTime.now();
    }
}