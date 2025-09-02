package com.tien.orderservice.messaging;

import com.tien.orderservice.entity.OrderStatus;
import com.tien.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCapturedListener {
    OrderRepository orderRepository;

    @KafkaListener(topics = "payment.captured", groupId = "order-service")
    public void onMessage(String message){
        try{
            JSONObject json = new JSONObject(message);
            Long orderId = json.getLong("orderId");
            var o = orderRepository.findById(orderId).orElse(null);
            if (o != null){
                o.setStatus(OrderStatus.PAID);
                orderRepository.save(o);
            }
        }catch(Exception ignored){}
    }
}
