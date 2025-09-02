package com.tien.inventoryservice.repository;

import com.tien.inventoryservice.entity.ProductStock;
import com.tien.inventoryservice.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByWarehouseAndProductId(Warehouse warehouse, Long productId);
    List<ProductStock> findByProductId(Long productId);
    Page<ProductStock> findAll(Pageable pageable);
    Page<ProductStock> findByProductId(Long productId, Pageable pageable);
    Page<ProductStock> findByWarehouse_Id(Long warehouseId, Pageable pageable);
    Page<ProductStock> findByProductIdAndWarehouse_Id(Long productId, Long warehouseId, Pageable pageable);
}