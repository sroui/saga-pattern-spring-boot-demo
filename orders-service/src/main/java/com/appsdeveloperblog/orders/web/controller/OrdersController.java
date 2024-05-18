package com.appsdeveloperblog.orders.web.controller;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.orders.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
    }
}
