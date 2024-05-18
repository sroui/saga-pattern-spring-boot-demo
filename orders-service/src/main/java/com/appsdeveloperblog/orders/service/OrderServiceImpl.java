package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.core.types.OrderStatus;
import com.appsdeveloperblog.orders.dao.jpa.entity.OrderEntity;
import com.appsdeveloperblog.orders.dao.jpa.repository.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String ordersEventsTopicName;

    public OrderServiceImpl(OrderRepository orderRepository,
                            KafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${orders.events.topic.name}") String ordersEventsTopicName) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.ordersEventsTopicName = ordersEventsTopicName;
    }

    @Override
    public void placeOrder(Order order) {
        order.setStatus(OrderStatus.CREATED);
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntity);
        orderRepository.save(orderEntity);

        order.getProduct().setCustomerId(order.getCustomerId());
        order.setId(orderEntity.getId());
        order.getProduct().setOrderId(orderEntity.getId());

        kafkaTemplate.send(ordersEventsTopicName, order);
    }

    @Override
    public void approveOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(orderEntity, "no order found with id " + orderEntity + " in the database");
        orderEntity.setStatus(OrderStatus.APPROVED);
        orderRepository.save(orderEntity);

        Order order = new Order();
        BeanUtils.copyProperties(orderEntity, order);
        kafkaTemplate.send(ordersEventsTopicName, order);
    }
}
