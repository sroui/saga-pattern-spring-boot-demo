package com.appsdeveloperblog.core.dto.events;

public class ShipmentTicketCreatedEvent {
    private Long orderId;

    public ShipmentTicketCreatedEvent() {
    }

    public ShipmentTicketCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
