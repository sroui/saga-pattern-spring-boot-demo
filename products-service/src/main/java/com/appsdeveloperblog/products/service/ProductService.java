package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    void reserve(Product product, UUID orderId);

    Product save(Product product);
}
