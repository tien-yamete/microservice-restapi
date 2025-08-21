package com.tien.inventoryservice.service;

import com.tien.inventoryservice.dto.request.ReservationCreateRequest;
import com.tien.inventoryservice.entity.ProductStock;
import com.tien.inventoryservice.entity.Reservation;
import com.tien.inventoryservice.entity.ReservationItem;
import com.tien.inventoryservice.entity.Warehouse;
import com.tien.inventoryservice.exception.AppException;
import com.tien.inventoryservice.exception.ErrorCode;
import com.tien.inventoryservice.repository.ProductStockRepository;
import com.tien.inventoryservice.repository.ReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationService {
    ReservationRepository repo;
    ProductStockRepository psRepo;
    WarehouseService warehouseService;

    @Transactional
    public Reservation create(ReservationCreateRequest req) {
        Reservation res = repo.findByOrderId(req.getOrderId()).orElseGet(Reservation::new);
        res.setOrderId(req.getOrderId());
        res.getItems().clear();
        Reservation finalRes = res;
        req.getItems().forEach(it -> finalRes.getItems().add(ReservationItem.builder().reservation(finalRes).productId(it.getProductId()).quantity(it.getQuantity()).build()));
        res.setStatus(Reservation.Status.PENDING);

        // Reserve from a single warehouse (req.warehouseId)
        Warehouse wh = warehouseService.require(req.getWarehouseId());
        for (var it: req.getItems()){
            ProductStock ps = psRepo.findByWarehouseAndProductId(wh, it.getProductId()).orElse(ProductStock.builder().warehouse(wh).productId(it.getProductId()).available(0).reserved(0).build());
            if (ps.getAvailable() < it.getQuantity()) throw new AppException(ErrorCode.INVALID_ARGUMENT);
        }
        for (var it: req.getItems()){
            ProductStock ps = psRepo.findByWarehouseAndProductId(wh, it.getProductId()).orElseThrow();
            ps.setAvailable(ps.getAvailable()-it.getQuantity());
            ps.setReserved(ps.getReserved()+it.getQuantity());
            psRepo.save(ps);
        }
        return repo.save(res);
    }

    @Transactional
    public void commit(Long orderId, Long warehouseId) {
        Reservation res = repo.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Warehouse wh = warehouseService.require(warehouseId);
        for (var it: res.getItems()){
            ProductStock ps = psRepo.findByWarehouseAndProductId(wh, it.getProductId()).orElseThrow();
            if (ps.getReserved() < it.getQuantity()) throw new AppException(ErrorCode.INVALID_ARGUMENT);
            ps.setReserved(ps.getReserved()-it.getQuantity());
            psRepo.save(ps);
        }
        res.setStatus(Reservation.Status.CONFIRMED);
        repo.save(res);
    }

    @Transactional
    public void release(Long orderId, Long warehouseId) {
        Reservation res = repo.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        Warehouse wh = warehouseService.require(warehouseId);
        for (var it: res.getItems()){
            ProductStock ps = psRepo.findByWarehouseAndProductId(wh, it.getProductId()).orElseThrow();
            ps.setAvailable(ps.getAvailable()+it.getQuantity());
            ps.setReserved(Math.max(0, ps.getReserved()-it.getQuantity()));
            psRepo.save(ps);
        }
        res.setStatus(Reservation.Status.RELEASED);
        repo.save(res);
    }
}