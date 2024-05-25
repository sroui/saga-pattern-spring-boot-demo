package com.appsdeveloperblog.core.dto.events;

public class PaymentProcessedEvent {
    private Long paymentId;
    private Long orderId;
    private Long customerId;
    private Long productId;

    public PaymentProcessedEvent() {
    }

    public PaymentProcessedEvent(Long paymentId, Long orderId, Long customerId, Long productId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
    }

    public Long getPaymentId() {
        return paymentId;
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

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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
