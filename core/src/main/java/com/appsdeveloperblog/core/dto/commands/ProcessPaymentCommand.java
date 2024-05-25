package com.appsdeveloperblog.core.dto.commands;

import java.math.BigDecimal;

public class ProcessPaymentCommand {
    private Long productId;
    private Long orderId;
    private Long customerId;
    private BigDecimal amount;

    public ProcessPaymentCommand() {
    }

    public ProcessPaymentCommand(Long productId, Long orderId, Long customerId, BigDecimal amount) {
        this.productId = productId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
