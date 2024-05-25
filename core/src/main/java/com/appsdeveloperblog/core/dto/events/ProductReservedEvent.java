package com.appsdeveloperblog.core.dto.events;

import java.math.BigDecimal;

public class ProductReservedEvent {
    private Long productId;
    private Long orderId;
    private Long customerId;
    private BigDecimal productPrice;

    public ProductReservedEvent() {
    }

    public ProductReservedEvent(Long productId, Long orderId, Long customerId, BigDecimal productPrice) {
        this.productId = productId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.productPrice = productPrice;
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

    public BigDecimal getProductPrice() {
        return productPrice;
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

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
}
