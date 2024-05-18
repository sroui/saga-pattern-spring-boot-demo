package com.appsdeveloperblog.core.dto;

import com.appsdeveloperblog.core.types.OrderCommandType;

import java.time.LocalDateTime;

public class OrderCommand {
    private Long orderId;
    private OrderCommandType type;
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderCommandType getType() {
        return type;
    }

    public void setType(OrderCommandType type) {
        this.type = type;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
