package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    void reserve(Product product, Long orderId);

    void save(Product product);
}
