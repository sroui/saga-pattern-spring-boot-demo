package com.appsdeveloperblog.core.dto.events;

import java.util.UUID;

public class ShipmentTicketCreatedEvent {
    private UUID orderId;

    public ShipmentTicketCreatedEvent() {
    }

    public ShipmentTicketCreatedEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
