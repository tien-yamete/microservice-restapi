package com.tien.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="reservation_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long productId;
    Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    Reservation reservation;
}