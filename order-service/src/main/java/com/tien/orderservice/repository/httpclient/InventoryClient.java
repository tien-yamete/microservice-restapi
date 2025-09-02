
package com.tien.orderservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tien.orderservice.dto.*; 
import com.tien.orderservice.dto.request.*;

@FeignClient(name = "inventory-service", url = "${app.services.inventory}")
public interface InventoryClient {
    @PostMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Long> reserve(@RequestBody ReservationCreateRequest request);

    @PostMapping(value = "/reservations/{orderId}/commit", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> commit(@PathVariable("orderId") Long orderId, @RequestParam("warehouseId") Long warehouseId);

    @PostMapping(value = "/reservations/{orderId}/release", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> release(@PathVariable("orderId") Long orderId, @RequestParam("warehouseId") Long warehouseId);
}
