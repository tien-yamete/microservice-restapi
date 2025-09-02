package com.tien.orderservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tien.orderservice.dto.ApiResponse;
import com.tien.orderservice.dto.response.ProductResponse;

@FeignClient(name = "product-service", url = "${app.services.product}")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ApiResponse<ProductResponse> get(@PathVariable("id") Long id);
}
