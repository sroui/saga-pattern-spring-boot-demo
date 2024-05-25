package com.appsdeveloperblog.payments.service;

import com.appsdeveloperblog.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    boolean isValid(Payment payment);

    void process(Payment payment);
}
