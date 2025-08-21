package com.tien.inventoryservice.controller;

import com.tien.inventoryservice.dto.ApiResponse;
import com.tien.inventoryservice.dto.request.WarehouseRequest;
import com.tien.inventoryservice.dto.response.WarehouseResponse;
import com.tien.inventoryservice.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/warehouses")
@RequiredArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseController {

    WarehouseService service;

    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseResponse>> create(@Valid @RequestBody WarehouseRequest req){
        return ResponseEntity.ok(ApiResponse.<WarehouseResponse>builder().result(service.create(req)).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> update(@PathVariable Long id, @RequestBody WarehouseRequest req){
        return ResponseEntity.ok(ApiResponse.<WarehouseResponse>builder().result(service.update(id, req)).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> get(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.<WarehouseResponse>builder().result(service.get(id)).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> list(){
        return ResponseEntity.ok(ApiResponse.<List<WarehouseResponse>>builder().result(service.list()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").build());
    }
}