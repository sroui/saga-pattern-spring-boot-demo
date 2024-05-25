package com.appsdeveloperblog.core.dto;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private Long orderId;
    private Long customerId;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(Long id, Long orderId, Long customerId, String name, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
