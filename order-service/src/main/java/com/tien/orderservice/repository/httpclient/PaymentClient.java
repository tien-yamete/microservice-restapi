
package com.tien.orderservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tien.orderservice.dto.ApiResponse;

@FeignClient(name = "payment-service", url = "${app.services.payment}")
public interface PaymentClient {
    @PostMapping(value = "/api/payments/{orderId}/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> authorize(@PathVariable("orderId") Long orderId);

    @PostMapping(value = "/api/payments/{orderId}/capture", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> capture(@PathVariable("orderId") Long orderId);

    @PostMapping(value = "/api/payments/{orderId}/refund", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> refund(@PathVariable("orderId") Long orderId);
}
