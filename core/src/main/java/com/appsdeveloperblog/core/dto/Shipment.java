package com.appsdeveloperblog.core.dto;

public class Shipment {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long customerId;

    public Shipment() {
    }

    public Shipment(Long id, Long orderId, Long productId, Long customerId) {
        this.id = id;
        this.orderId = orderId;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
