package com.appsdeveloperblog.core.dto;

import com.appsdeveloperblog.core.types.ProductCommandType;

import java.time.LocalDateTime;

public class ProductCommand {
    private Product product;
    private ProductCommandType type;
    private LocalDateTime createdAt;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductCommandType getType() {
        return type;
    }

    public void setType(ProductCommandType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
