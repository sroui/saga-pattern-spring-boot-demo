package com.appsdeveloperblog.core.dto.events;

public class OrderCreatedEvent {
    private Long orderId;
    private Long customerId;
    private Long productId;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long customerId, Long productId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
