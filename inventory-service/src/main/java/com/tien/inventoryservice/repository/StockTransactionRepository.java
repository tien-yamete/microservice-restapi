package com.tien.inventoryservice.repository;

import com.tien.inventoryservice.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {}