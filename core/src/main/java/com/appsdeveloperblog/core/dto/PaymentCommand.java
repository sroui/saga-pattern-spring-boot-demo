package com.appsdeveloperblog.core.dto;

import com.appsdeveloperblog.core.types.PaymentCommandType;

import java.time.LocalDateTime;

public class PaymentCommand {
    private PaymentCommandType type;
    private LocalDateTime createdAt;
    private Payment payment;

    public PaymentCommandType getType() {
        return type;
    }

    public void setType(PaymentCommandType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
