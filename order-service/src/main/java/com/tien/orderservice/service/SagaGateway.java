package com.tien.orderservice.service;

import com.tien.orderservice.entity.Order;

public interface SagaGateway {
    void start(Order order);
    void compensateAfterCancel(Order order);
}