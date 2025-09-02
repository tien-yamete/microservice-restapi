package com.tien.orderservice.service;

import com.tien.orderservice.dto.request.OrderCreateRequest;
import com.tien.orderservice.dto.request.OrderSearchRequest;
import com.tien.orderservice.dto.request.PaymentCreateRequest;
import com.tien.orderservice.dto.request.ReservationCreateRequest;
import com.tien.orderservice.dto.response.OrderItemResponse;
import com.tien.orderservice.dto.response.OrderResponse;
import com.tien.orderservice.entity.Order;
import com.tien.orderservice.entity.OrderItem;
import com.tien.orderservice.entity.OrderStatus;
import com.tien.orderservice.repository.OrderRepository;
import com.tien.orderservice.repository.httpclient.InventoryClient;
import com.tien.orderservice.repository.httpclient.PaymentClient;
import com.tien.orderservice.repository.httpclient.ProductClient;
import com.tien.orderservice.repository.spec.OrderSpecifications;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    InventoryClient inventoryClient;
    PaymentClient paymentClient;
    ProductClient productClient;

    private OrderResponse map(Order o){
        return OrderResponse.builder()
                .id(o.getId())
                .userId(o.getUserId())
                .amount(o.getAmount())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .items(o.getItems()==null? List.of() : o.getItems().stream()
                        .map(it -> OrderItemResponse.builder()
                                .productId(it.getProductId())
                                .quantity(it.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest req){
        // 1) Create Order entity
        Order o = new Order();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        log.info("user id : {}", userId);
        o.setUserId(userId);

        // Calculate amount from product-service
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        if (req.getItems()!=null){
            for (var i : req.getItems()){
                var pr = productClient.get(i.getProductId()).getResult();
                if (pr!=null && pr.getPrice()!=null){
                    total = total.add(pr.getPrice().multiply(new java.math.BigDecimal(i.getQuantity())));
                }
            }
        }
        o.setAmount(total);
        o.setStatus(OrderStatus.PENDING);
        o.setWarehouseId(req.getWarehouseId()==null? 1L : req.getWarehouseId());

        // Build items without capturing 'o' inside lambda
        if (req.getItems()!=null){
            List<OrderItem> items = req.getItems().stream()
                    .map(i -> OrderItem.builder()
                            .productId(i.getProductId())
                            .quantity(i.getQuantity())
                            .build())
                    .collect(Collectors.toList());
            for (OrderItem it : items) {
                it.setOrder(o);
            }
            o.setItems(items);
        }

        o = orderRepository.save(o);

        // 2) Reserve inventory (best-effort)
        try {
            ReservationCreateRequest r = ReservationCreateRequest.builder()
                    .orderId(o.getId())
                    .warehouseId(o.getWarehouseId())
                    .items(req.getItems()==null? List.of() : req.getItems().stream().map(i ->
                            ReservationCreateRequest.Item.builder()
                                    .productId(i.getProductId())
                                    .quantity(i.getQuantity())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build();
            inventoryClient.reserve(r);
        } catch (Exception ignore){}

        // 3) Create payment (best-effort, default method null => COD fallback)
        try {
            PaymentCreateRequest p = PaymentCreateRequest.builder()
                    .orderId(o.getId())
                    .userId(o.getUserId())
                    .amount(o.getAmount())
                    .method(null)
                    .build();
            paymentClient.create(p);
        } catch (Exception ignore){}

        return map(o);
    }

    public OrderResponse get(Long id){
        Order o = orderRepository.findById(id).orElseThrow();
        return map(o);
    }

    public Page<OrderResponse> search(OrderSearchRequest p){
        Sort sort = Sort.by(("desc".equalsIgnoreCase(p.getSortDir())? Sort.Direction.DESC : Sort.Direction.ASC),
                p.getSortBy()==null? "createdAt": p.getSortBy());
        Pageable pageable = PageRequest.of(p.getPage()==null?0:p.getPage(), p.getSize()==null?20:p.getSize(), sort);
        var spec = OrderSpecifications.build(p.getUserId(), p.getStatus(), p.getMinAmount(), p.getMaxAmount());
        Page<Order> page = orderRepository.findAll(spec, pageable);
        List<OrderResponse> data = page.getContent().stream().map(this::map).collect(Collectors.toList());
        return new PageImpl<>(data, pageable, page.getTotalElements());
    }

    @Transactional
    public void cancel(Long id){
        Order o = orderRepository.findById(id).orElseThrow();
        o.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(o);
        try { inventoryClient.release(id, o.getWarehouseId()); } catch (Exception ignore) {}
        try { paymentClient.refund(id); } catch (Exception ignore) {}
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status){
        Order o = orderRepository.findById(id).orElseThrow();
        o.setStatus(status);
        o = orderRepository.save(o);
        return map(o);
    }

    @Transactional
    public void cancelOrder(Long id){
        cancel(id);
    }
}
