package com.appsdeveloperblog.core.dto.events;

import java.util.UUID;

public class ProductReservationCanceledEvent {
    private UUID productId;
    private UUID orderId;

    public ProductReservationCanceledEvent() {
    }

    public ProductReservationCanceledEvent(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
