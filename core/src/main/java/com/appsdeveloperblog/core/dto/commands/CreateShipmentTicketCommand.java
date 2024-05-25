package com.appsdeveloperblog.core.dto.commands;

public class CreateShipmentTicketCommand {
    private Long orderId;
    private Long customerId;
    private Long productId;

    public CreateShipmentTicketCommand() {
    }

    public CreateShipmentTicketCommand(Long orderId, Long customerId, Long productId) {
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
