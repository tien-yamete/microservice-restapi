package com.tien.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable=false, unique=true, length=100)
    String code;
    @Column(nullable=false, length=255)
    String name;
    @Column(length=255)
    String address;
    @Column(length=50)
    String region; // optional
}