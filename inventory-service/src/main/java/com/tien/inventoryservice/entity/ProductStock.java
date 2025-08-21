package com.tien.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="product_stock", uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id","product_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable=false)
    Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="warehouse_id", nullable=false)
    Warehouse warehouse;

    @Column(nullable=false)
    Integer available;

    @Column(nullable=false)
    Integer reserved;

    @Version
    Long version;
}