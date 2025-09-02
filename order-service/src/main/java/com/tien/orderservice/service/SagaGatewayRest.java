
package com.tien.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.tien.orderservice.entity.Order;
import com.tien.orderservice.repository.httpclient.InventoryClient;
import com.tien.orderservice.repository.httpclient.PaymentClient;
import com.tien.orderservice.dto.request.ReservationCreateRequest;
import feign.FeignException;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaGatewayRest  {
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    private static final Long DEFAULT_WAREHOUSE_ID = 1L;

    public void start(Order order) {
        log.info("[SAGA-REST] Start order {}", order.getId());
        try {
            // Step 0: create payment record if not exists
            paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .method("COD")
                    .amount(order.getAmount())
                    .build());
            // Step 1: Reserve inventory
            ReservationCreateRequest req = ReservationCreateRequest.builder()
                    .orderId(order.getId())
                    .warehouseId(DEFAULT_WAREHOUSE_ID)
                    .items(order.getItems().stream().map(i -> ReservationCreateRequest.Item.builder()
                            .productId(i.getProductId())
                            .quantity(i.getQuantity())
                            .build()).collect(Collectors.toList()))
                    .build();
            inventoryClient.reserve(req);
            log.info("[SAGA-REST] Reserved inventory for order {}", order.getId());

            // Step 2: Authorize payment
            paymentClient.authorize(order.getId());
            log.info("[SAGA-REST] Authorized payment for order {}", order.getId());

            // Step 3: Capture payment
            paymentClient.capture(order.getId());
            log.info("[SAGA-REST] Captured payment for order {}", order.getId());

            // Step 4: Commit inventory
            inventoryClient.commit(order.getId(), DEFAULT_WAREHOUSE_ID);
            log.info("[SAGA-REST] Committed inventory for order {}", order.getId());

        } catch (FeignException e) {
            log.error("[SAGA-REST] Step failed for order {}: {} - compensating", order.getId(), e.contentUTF8());
            // Compensation: refund and release reservation
            try {
                // Step 0: create payment record if not exists
                paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .method("COD")
                        .amount(order.getAmount())
                        .build()); paymentClient.refund(order.getId()); } catch (Exception ignore) { log.warn("Refund failed/ignored"); }
            try {
                // Step 0: create payment record if not exists
                paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .method("COD")
                        .amount(order.getAmount())
                        .build()); inventoryClient.release(order.getId(), DEFAULT_WAREHOUSE_ID); } catch (Exception ignore) { log.warn("Release failed/ignored"); }
            throw e;
        } catch (RuntimeException e) {
            log.error("[SAGA-REST] Runtime failure for order {}: {}", order.getId(), e.getMessage());
            try {
                // Step 0: create payment record if not exists
                paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .method("COD")
                        .amount(order.getAmount())
                        .build()); paymentClient.refund(order.getId()); } catch (Exception ignore) { }
            try {
                // Step 0: create payment record if not exists
                paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                        .orderId(order.getId())
                        .userId(order.getUserId())
                        .method("COD")
                        .amount(order.getAmount())
                        .build()); inventoryClient.release(order.getId(), DEFAULT_WAREHOUSE_ID); } catch (Exception ignore) { }
            throw e;
        }
    }

    public void compensateAfterCancel(Order order) {
        log.info("[SAGA-REST] Compensate after cancel order {}", order.getId());
        try {
            // Step 0: create payment record if not exists
            paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .method("COD")
                    .amount(order.getAmount())
                    .build()); inventoryClient.release(order.getId(), DEFAULT_WAREHOUSE_ID); } catch (Exception ignore) { }
        try {
            // Step 0: create payment record if not exists
            paymentClient.create(com.tien.orderservice.dto.request.PaymentCreateRequest.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .method("COD")
                    .amount(order.getAmount())
                    .build()); paymentClient.refund(order.getId()); } catch (Exception ignore) { }
    }
}
