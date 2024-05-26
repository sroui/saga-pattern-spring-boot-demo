package com.appsdeveloperblog.core.dto;

import java.math.BigDecimal;

public class Payment {
    private Long id;
    private Long orderId;
    private Long productId;
    private BigDecimal productPrice;
    private Integer productQuantity;

    public Payment() {
    }

    public Payment(Long id, Long orderId, Long productId, BigDecimal productPrice, Integer productQuantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }


    public Payment(Long orderId, Long productId, BigDecimal productPrice, Integer productQuantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}

