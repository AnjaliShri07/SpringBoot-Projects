package com.PaymentService.PaymentProcessing.controller;

import com.PaymentService.PaymentProcessing.constant.PaymentStatus;
import com.PaymentService.PaymentProcessing.entity.Payment;
import com.PaymentService.PaymentProcessing.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment paymentRequest) {
        return ResponseEntity.ok(paymentService.createPayment(paymentRequest));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentStatus(@PathVariable Long paymentId) {
        return paymentService.getPaymentById(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long paymentId, @RequestBody String status) {
        //PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status));
    }
}
