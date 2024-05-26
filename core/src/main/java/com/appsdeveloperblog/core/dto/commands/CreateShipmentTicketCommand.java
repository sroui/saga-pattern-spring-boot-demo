package com.appsdeveloperblog.core.dto.commands;

public class CreateShipmentTicketCommand {
    private Long orderId;
    private Long paymentId;

    public CreateShipmentTicketCommand() {
    }

    public CreateShipmentTicketCommand(Long orderId, Long paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
