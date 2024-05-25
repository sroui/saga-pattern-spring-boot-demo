package com.appsdeveloperblog.core.dto.events;

public class OrderApprovedEvent {
    private Long orderId;

    public OrderApprovedEvent() {
    }

    public OrderApprovedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
