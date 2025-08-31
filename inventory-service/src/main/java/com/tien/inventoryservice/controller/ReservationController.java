package com.tien.inventoryservice.controller;

import com.tien.inventoryservice.dto.ApiResponse;
import com.tien.inventoryservice.dto.request.ReservationCreateRequest;
import com.tien.inventoryservice.entity.Reservation;
import com.tien.inventoryservice.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationController {

    ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(@Valid @RequestBody ReservationCreateRequest req){
        Reservation r = reservationService.create(req);
        return ResponseEntity.ok(ApiResponse.<Long>builder().result(r.getId()).build());
    }

    @PostMapping("/{orderId}/commit")
    public ResponseEntity<ApiResponse<?>> commit(@PathVariable Long orderId, @RequestParam Long warehouseId){
        reservationService.commit(orderId, warehouseId);
        return ResponseEntity.ok(ApiResponse.builder().message("Committed").build());
    }

    @PostMapping("/{orderId}/release")
    public ResponseEntity<ApiResponse<?>> release(@PathVariable Long orderId, @RequestParam Long warehouseId){
        reservationService.release(orderId, warehouseId);
        return ResponseEntity.ok(ApiResponse.builder().message("Released").build());
    }
}