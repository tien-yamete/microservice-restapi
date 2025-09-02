package com.tien.paymentservice.service;

import com.tien.paymentservice.entity.Payment;
import com.tien.paymentservice.entity.PaymentMethod;
import com.tien.paymentservice.entity.PaymentStatus;

import com.tien.paymentservice.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    PaymentRepository paymentRepository;
    org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public Payment create(Long orderId, String customerId, BigDecimal amount, PaymentMethod method){
        if (method == null) method = PaymentMethod.COD;
        Payment p = Payment.builder()
                .orderId(orderId)
                .customerId(customerId)
                .amount(amount)
                .method(method)
                .status(PaymentStatus.CREATED)
                .build();
        return paymentRepository.save(p);   // <- không code gì sau return
    }

    @Transactional
    public void authorize(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow();
        p.setStatus(PaymentStatus.AUTHORIZED);
        p.setExternalAuthId("SIM-AUTH-" + Instant.now().toEpochMilli());
        paymentRepository.save(p);
    }

    @Transactional
    public void capture(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow();
        p.setStatus(PaymentStatus.CAPTURED);
        p.setExternalCaptureId("SIM-CAP-" + Instant.now().toEpochMilli());
        paymentRepository.save(p);

        // phát sự kiện sau khi lưu thành công
        String payload = "{\"type\":\"payment.captured\",\"orderId\":" + orderId + "}";
        kafkaTemplate.send("payment.captured", payload);
    }

    @Transactional
    public void refund(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow();
        p.setStatus(PaymentStatus.REFUNDED);
        p.setExternalRefundId("SIM-REF-" + Instant.now().toEpochMilli());
        paymentRepository.save(p);
    }
}
