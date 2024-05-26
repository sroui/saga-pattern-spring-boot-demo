package com.appsdeveloperblog.core.dto.commands;

import java.math.BigDecimal;

public class ProcessPaymentCommand {
    private Long orderId;
    private Long productId;
    private BigDecimal productPrice;
    private Integer productQuantity;

    public ProcessPaymentCommand() {
    }

    public ProcessPaymentCommand(Long orderId, Long productId, BigDecimal productPrice, Integer productQuantity) {
        this.productId = productId;
        this.orderId = orderId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
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
