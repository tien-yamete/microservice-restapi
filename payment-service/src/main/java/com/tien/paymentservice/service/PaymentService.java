package com.tien.paymentservice.service;

import com.tien.paymentservice.entity.Payment;
import com.tien.paymentservice.entity.PaymentMethod;
import com.tien.paymentservice.entity.PaymentStatus;
import com.tien.paymentservice.exception.AppException;
import com.tien.paymentservice.exception.ErrorCode;
import com.tien.paymentservice.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service @RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    PaymentRepository paymentRepository;

    @Transactional
    public Payment ensure(Long orderId, String customerId, BigDecimal amount, PaymentMethod method){
        return paymentRepository.findByOrderId(orderId).orElseGet(() ->
            paymentRepository.save(Payment.builder().orderId(orderId).customerId(customerId).amount(amount).method(method).status(PaymentStatus.CREATED).build())
        );
    }

    @Transactional
    public Payment authorize(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        // mock authorize success if amount >= 0
        if (p.getAmount() == null || p.getAmount().signum() < 0) {
            p.setStatus(PaymentStatus.FAILED);
        } else {
            p.setStatus(PaymentStatus.AUTHORIZED);
            p.setExternalAuthId("AUTH-" + orderId);
        }
        return paymentRepository.save(p);
    }

    @Transactional
    public Payment capture(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (p.getStatus() != PaymentStatus.AUTHORIZED) throw new AppException(ErrorCode.INVALID_ARGUMENT);
        p.setStatus(PaymentStatus.CAPTURED);
        p.setExternalCaptureId("CAP-" + orderId);
        return paymentRepository.save(p);
    }

    @Transactional
    public Payment refund(Long orderId){
        Payment p = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        if (p.getStatus() == PaymentStatus.CAPTURED || p.getStatus() == PaymentStatus.AUTHORIZED) {
            p.setStatus(PaymentStatus.REFUNDED);
            p.setExternalRefundId("REF-" + orderId);
            return paymentRepository.save(p);
        }
        return p;
    }
}