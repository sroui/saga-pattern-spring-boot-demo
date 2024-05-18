package com.appsdeveloperblog.core.dto;

import com.appsdeveloperblog.core.types.OrderStatus;

public class Order {
    private Long id;
    private Long customerId;
    private OrderStatus status;
    private Product product;


    public Product getProduct() {
        return product;
    }

    public void setProducts(Product product) {
        this.product = product;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
