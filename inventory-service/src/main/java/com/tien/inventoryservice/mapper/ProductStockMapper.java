package com.tien.inventoryservice.mapper;

import com.tien.inventoryservice.dto.response.ProductStockResponse;
import com.tien.inventoryservice.entity.ProductStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductStockMapper {
    @Mapping(source = "warehouse.id", target = "warehouseId")
    ProductStockResponse toPeProductStock(ProductStock ps);
}