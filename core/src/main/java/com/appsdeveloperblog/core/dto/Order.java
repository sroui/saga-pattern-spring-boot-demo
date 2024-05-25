package com.appsdeveloperblog.core.dto;


import com.appsdeveloperblog.core.types.OrderStatus;

public class Order {
    private Long id;
    private Long customerId;
    private Long productId;

    public Order() {
    }

    public Order(Long id, Long customerId, OrderStatus status, Long productId) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
