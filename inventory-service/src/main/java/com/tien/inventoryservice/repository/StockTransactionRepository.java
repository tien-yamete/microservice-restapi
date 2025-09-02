package com.tien.inventoryservice.repository;

import com.tien.inventoryservice.entity.StockTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    @Query(" SELECT t FROM StockTransaction t WHERE t.productId = :productId AND (t.warehouseFromId = :warehouseId OR t.warehouseToId = :warehouseId)ORDER BY t.createdAt DESC")
    Page<StockTransaction> findLedger(@Param("productId") Long productId,
                                      @Param("warehouseId") Long warehouseId,
                                      Pageable pageable);
}