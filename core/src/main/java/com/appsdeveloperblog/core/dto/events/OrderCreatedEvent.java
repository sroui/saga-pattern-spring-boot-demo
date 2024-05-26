package com.appsdeveloperblog.core.dto.events;

public class OrderCreatedEvent {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private Integer productQuantity;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long customerId, Long productId, Integer productQuantity) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.productQuantity = productQuantity;
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

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}
