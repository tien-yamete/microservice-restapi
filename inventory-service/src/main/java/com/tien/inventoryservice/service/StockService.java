package com.tien.inventoryservice.service;

import com.tien.inventoryservice.dto.request.StockAdjustRequest;
import com.tien.inventoryservice.dto.request.StockMoveRequest;
import com.tien.inventoryservice.dto.response.ProductStockResponse;
import com.tien.inventoryservice.entity.ProductStock;
import com.tien.inventoryservice.entity.StockTransaction;
import com.tien.inventoryservice.entity.Warehouse;
import com.tien.inventoryservice.exception.AppException;
import com.tien.inventoryservice.exception.ErrorCode;
import com.tien.inventoryservice.mapper.ProductStockMapper;
import com.tien.inventoryservice.repository.ProductStockRepository;
import com.tien.inventoryservice.repository.StockTransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockService {
    ProductStockRepository psRepo;
    StockTransactionRepository txRepo;
    WarehouseService warehouseService;
    ProductStockMapper mapper;

    private ProductStock ensure(Warehouse wh, Long productId) {
        return psRepo.findByWarehouseAndProductId(wh, productId).orElseGet(() -> {
            ProductStock s = ProductStock.builder().warehouse(wh).productId(productId).available(0).reserved(0).build();
            return psRepo.save(s);
        });
    }

    @Transactional
    public ProductStockResponse adjust(StockAdjustRequest req) {
        Warehouse wh = warehouseService.require(req.getWarehouseId());
        ProductStock st = ensure(wh, req.getProductId());
        if (req.getType() == StockAdjustRequest.Type.IN) {
            st.setAvailable(st.getAvailable() + req.getQuantity());
            txRepo.save(StockTransaction.builder().type(StockTransaction.Type.ADJUST_IN).productId(req.getProductId()).warehouseToId(wh.getId()).quantity(req.getQuantity()).reason(req.getReason()).build());
        } else {
            if (st.getAvailable() < req.getQuantity()) throw new AppException(ErrorCode.INVALID_ARGUMENT);
            st.setAvailable(st.getAvailable() - req.getQuantity());
            txRepo.save(StockTransaction.builder().type(StockTransaction.Type.ADJUST_OUT).productId(req.getProductId()).warehouseFromId(wh.getId()).quantity(req.getQuantity()).reason(req.getReason()).build());
        }
        psRepo.save(st);
        return mapper.toPeProductStock(st);
    }

    @Transactional
    public void move(StockMoveRequest req) {
        Warehouse from = warehouseService.require(req.getWarehouseFromId());
        Warehouse to = warehouseService.require(req.getWarehouseToId());
        if (from.getId().equals(to.getId())) throw new AppException(ErrorCode.INVALID_ARGUMENT);
        ProductStock sFrom = ensure(from, req.getProductId());
        if (sFrom.getAvailable() < req.getQuantity()) throw new AppException(ErrorCode.INVALID_ARGUMENT);
        sFrom.setAvailable(sFrom.getAvailable()-req.getQuantity());
        psRepo.save(sFrom);
        ProductStock sTo = ensure(to, req.getProductId());
        sTo.setAvailable(sTo.getAvailable()+req.getQuantity());
        psRepo.save(sTo);
        txRepo.save(StockTransaction.builder().type(StockTransaction.Type.MOVE).productId(req.getProductId()).warehouseFromId(from.getId()).warehouseToId(to.getId()).quantity(req.getQuantity()).reason(req.getReason()).build());
    }

    public List<ProductStockResponse> listByProduct(Long productId){
        return psRepo.findByProductId(productId).stream().map(mapper::toPeProductStock).toList();
    }
}