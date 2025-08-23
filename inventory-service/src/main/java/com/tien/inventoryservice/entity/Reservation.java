package com.tien.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long orderId;

    @Enumerated(EnumType.STRING)
    Status status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy="reservation", cascade=CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    List<ReservationItem> items = new ArrayList<>();

    public enum Status { PENDING, CONFIRMED, RELEASED }

    @PrePersist
    void pre(){
        createdAt = updatedAt = LocalDateTime.now();
        if(status==null)
            status = Status.PENDING;
    }
    @PreUpdate
    void upd(){
        updatedAt = LocalDateTime.now();
    }
}