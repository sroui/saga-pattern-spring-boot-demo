package com.appsdeveloperblog.core.dto;

public class Shipment {
    private Long id;
    private Long orderId;
    private Long paymentId;

    public Shipment() {
    }

    public Shipment(Long id, Long orderId, Long paymentId) {
        this.id = id;
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public Shipment(Long orderId, Long paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
