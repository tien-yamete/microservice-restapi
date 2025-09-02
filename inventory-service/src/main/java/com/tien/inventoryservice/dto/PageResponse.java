package com.tien.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private int page;
    private int size;
    private long total;
    private List<T> items;
}
