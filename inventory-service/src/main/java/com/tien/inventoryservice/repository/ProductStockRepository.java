package com.tien.inventoryservice.repository;

import com.tien.inventoryservice.entity.ProductStock;
import com.tien.inventoryservice.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByWarehouseAndProductId(Warehouse warehouse, Long productId);
    List<ProductStock> findByProductId(Long productId);
}