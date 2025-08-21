package com.tien.orderservice.service;

import com.tien.orderservice.dto.*;
import com.tien.orderservice.dto.request.OrderCreateRequest;
import com.tien.orderservice.dto.request.OrderSearchRequest;
import com.tien.orderservice.dto.response.OrderItemResponse;
import com.tien.orderservice.dto.response.OrderResponse;
import com.tien.orderservice.entity.Order;
import com.tien.orderservice.entity.OrderItem;
import com.tien.orderservice.entity.OrderStatus;
import com.tien.orderservice.exception.AppException;
import com.tien.orderservice.exception.ErrorCode;
import com.tien.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    SagaGateway sagaGateway;

    @Transactional
    public OrderResponse create(OrderCreateRequest req) {
        Order order = Order.builder()
                .customerId(req.getCustomerId())
                .amount(req.getAmount())
                .build();
        order = orderRepository.save(order);
        Long orderId = order.getId();
        for (var it : req.getItems()) {
            order.getItems().add(OrderItem.builder().order(order).productId(it.getProductId()).quantity(it.getQuantity()).build());
        }
        order = orderRepository.save(order);
        sagaGateway.start(order);
        return toResponse(order);
    }

    public OrderResponse get(Long id){
        Order o = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return toResponse(o);
    }

    public Page<OrderResponse> search(OrderSearchRequest p){
        Specification<Order> spec = (root, q, cb) -> cb.conjunction();

        if (p.getCustomerId() != null && !p.getCustomerId().isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("customerId"), p.getCustomerId()));
        }
        if (p.getStatus() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), p.getStatus()));
        }
        if (p.getMinAmount() != null) {
            spec = spec.and((root, q, cb) -> cb.ge(root.get("amount"), p.getMinAmount()));
        }
        if (p.getMaxAmount() != null) {
            spec = spec.and((root, q, cb) -> cb.le(root.get("amount"), p.getMaxAmount()));
        }
        Sort sort = Sort.by((p.getSortDir()!=null && p.getSortDir().equalsIgnoreCase("asc"))? Sort.Direction.ASC: Sort.Direction.DESC,
                p.getSortBy()!=null? p.getSortBy(): "createdAt");
        Pageable pageable = PageRequest.of(p.getPage()!=null? Math.max(0,p.getPage()):0, p.getSize()!=null? Math.min(100,p.getSize()):20, sort);
        return orderRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional
    public void cancel(Long id){
        Order o = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (o.getStatus() == OrderStatus.COMPLETED || o.getStatus() == OrderStatus.CANCELLED) return;
        o.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(o);
        // optional: publish compensation (release + refund) if needed
        sagaGateway.compensateAfterCancel(o);
    }

    public OrderResponse toResponse(Order o){
        return OrderResponse.builder()
                .id(o.getId()).customerId(o.getCustomerId()).amount(o.getAmount()).status(o.getStatus())
                .createdAt(o.getCreatedAt()).updatedAt(o.getUpdatedAt())
                .items(o.getItems().stream().map(i -> OrderItemResponse.builder().productId(i.getProductId()).quantity(i.getQuantity()).build()).collect(Collectors.toList()))
                .build();
    }
}