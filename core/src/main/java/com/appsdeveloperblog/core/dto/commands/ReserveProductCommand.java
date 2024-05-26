package com.appsdeveloperblog.core.dto.commands;

public class ReserveProductCommand {
    private Long orderId;
    private Long productId;
    private Integer productQuantity;

    public ReserveProductCommand() {
    }

    public ReserveProductCommand(Long orderId, Long productId, Integer productQuantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public Long getProductId() {
        return productId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
