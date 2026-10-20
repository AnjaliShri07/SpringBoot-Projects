package com.PaymentService.PaymentProcessing.service;

import com.PaymentService.PaymentProcessing.constant.Currency;
import com.PaymentService.PaymentProcessing.constant.PaymentStatus;
import com.PaymentService.PaymentProcessing.entity.Payment;
import com.PaymentService.PaymentProcessing.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment paymentRequest) {
        if (isValidCurrency(paymentRequest.getCurrency())) {
            return paymentRepository.save(paymentRequest);
        } else {
            throw new IllegalArgumentException("Invalid currency. Accepted values: USD, EUR, INR.");
        }
    }

    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public Payment updatePaymentStatus(Long paymentId, String newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        if (payment.getStatus().equals(newStatus)) {
            payment.setStatus(newStatus);
            return paymentRepository.save(payment);
        } else {
            throw new IllegalStateException("Payment status can only be updated if it is PENDING");
        }
    }

    private boolean isValidCurrency(String currency) {
        try {
            Currency.valueOf(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
