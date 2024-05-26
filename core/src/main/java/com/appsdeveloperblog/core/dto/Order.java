package com.appsdeveloperblog.core.dto;


public class Order {
    private Long customerId;
    private Long productId;
    private Integer productQuantity;

    public Order() {
    }

    public Order(Long customerId, Long productId, Integer productQuantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCustomerId() {
        return customerId;
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
