package com.tien.inventoryservice.service;

import com.tien.inventoryservice.dto.request.WarehouseRequest;
import com.tien.inventoryservice.dto.response.WarehouseResponse;
import com.tien.inventoryservice.entity.Warehouse;
import com.tien.inventoryservice.exception.AppException;
import com.tien.inventoryservice.exception.ErrorCode;
import com.tien.inventoryservice.mapper.WarehouseMapper;
import com.tien.inventoryservice.repository.WarehouseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseService {
    WarehouseRepository repo;
    WarehouseMapper mapper;

    public WarehouseResponse create(WarehouseRequest req) {
        if (repo.existsByCode(req.getCode())) throw new AppException(ErrorCode.DUPLICATED);
        Warehouse wh = mapper.toWarehouse(req);
        repo.save(wh);
        return mapper.toWarehouseResponse(wh);
    }

    public WarehouseResponse update(Long id, WarehouseRequest req) {
        Warehouse wh = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (req.getCode()!=null) wh.setCode(req.getCode());
        if (req.getName()!=null) wh.setName(req.getName());
        if (req.getAddress()!=null) wh.setAddress(req.getAddress());
        if (req.getRegion()!=null) wh.setRegion(req.getRegion());
        repo.save(wh);
        return mapper.toWarehouseResponse(wh);
    }

    public WarehouseResponse get(Long id){ return mapper.toWarehouseResponse(repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND))); }
    public List<WarehouseResponse> list(){ return repo.findAll().stream().map(mapper::toWarehouseResponse).toList(); }
    public void delete(Long id){ repo.deleteById(id); }
    public Warehouse require(Long id){ return repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)); }
}