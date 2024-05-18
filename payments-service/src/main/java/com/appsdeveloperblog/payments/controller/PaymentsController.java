package com.appsdeveloperblog.payments.controller;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.payments.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Payment> placeOrder() {
        return paymentService.findAll();
    }
}
