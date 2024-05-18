package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.core.dto.Order;

public interface OrderService {
    void placeOrder(Order order);
    void approveOrder(Long orderId);
}
