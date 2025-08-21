package com.tien.inventoryservice.mapper;

import com.tien.inventoryservice.dto.request.WarehouseRequest;
import com.tien.inventoryservice.dto.response.WarehouseResponse;
import com.tien.inventoryservice.entity.Warehouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toWarehouse(WarehouseRequest req);
    WarehouseResponse toWarehouseResponse(Warehouse wh);
}