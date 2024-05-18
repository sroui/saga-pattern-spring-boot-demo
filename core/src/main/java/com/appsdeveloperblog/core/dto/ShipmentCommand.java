package com.appsdeveloperblog.core.dto;

import com.appsdeveloperblog.core.types.ShipmentCommandType;

import java.time.LocalDateTime;

public class ShipmentCommand {
    private ShipmentCommandType type;
    private LocalDateTime createdAt;
    private Shipment shipment;

    public ShipmentCommandType getType() {
        return type;
    }

    public void setType(ShipmentCommandType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
