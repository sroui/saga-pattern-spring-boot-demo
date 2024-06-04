package com.appsdeveloperblog.orders.web.controller;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.orders.dto.CreateOrderRequest;
import com.appsdeveloperblog.orders.dto.CreateOrderResponse;
import com.appsdeveloperblog.orders.service.OrderService;
import org.springframework.beans.BeanUtils;
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateOrderResponse placeOrder(@RequestBody CreateOrderRequest request) {
        var order = new Order();
        BeanUtils.copyProperties(request, order);
        Order createdOrder = orderService.placeOrder(order);

        var response = new CreateOrderResponse();
        BeanUtils.copyProperties(createdOrder, response);
        return response;
    }
}
