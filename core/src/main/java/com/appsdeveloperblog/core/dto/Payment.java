package com.appsdeveloperblog.core.dto;

import java.math.BigDecimal;

public class Payment {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private Long productId;
    private Long customerId;

    public Payment() {
    }

    public Payment(Long id, Long orderId, Long customerId, Long productId, BigDecimal amount) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.productId = productId;
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
