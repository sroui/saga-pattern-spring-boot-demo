package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.*;
import com.appsdeveloperblog.core.types.*;
import com.appsdeveloperblog.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@KafkaListener(topics = {
        "${orders.events.topic.name}",
        "${products.events.topic.name}",
        "${payments.events.topic.name}",
        "${shipments.events.topic.name}",
        "${orders.commands.topic.name}"
})
public class CreateOrderSaga {
    @Autowired
    private OrderService orderService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${orders.commands.topic.name}")
    private String ordersCommandsTopicName;
    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;
    @Value("${shipments.commands.topic.name}")
    private String shipmentsCommandsTopicName;

    @KafkaHandler
    public void handleEvent(@Payload Order order) {
        if (order.getStatus().equals(OrderStatus.CREATED)) {
            ProductCommand command = new ProductCommand();
            command.setType(ProductCommandType.RESERVE_PRODUCT);
            command.setCreatedAt(LocalDateTime.now());
            command.setProduct(order.getProduct());
            kafkaTemplate.send(productsCommandsTopicName, command);
        }
    }

    @KafkaHandler
    public void handleEvent(@Payload Product product) {
        Payment payment = new Payment();
        payment.setOrderId(product.getOrderId());
        payment.setProductId(product.getId());
        payment.setCustomerId(product.getCustomerId());
        payment.setAmount(product.getPrice());
        PaymentCommand command = new PaymentCommand();
        command.setPayment(payment);
        command.setCreatedAt(LocalDateTime.now());
        command.setType(PaymentCommandType.PROCESS_PAYMENT);
        kafkaTemplate.send(paymentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload Payment payment) {
        Shipment shipment = new Shipment();
        shipment.setOrderId(payment.getOrderId());
        shipment.setCustomerId(payment.getCustomerId());
        shipment.setProductId(payment.getProductId());
        ShipmentCommand command = new ShipmentCommand();
        command.setType(ShipmentCommandType.CREATE_SHIPMENT_TICKET);
        command.setCreatedAt(LocalDateTime.now());
        command.setShipment(shipment);
        kafkaTemplate.send(shipmentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload Shipment shipment) {
        OrderCommand command = new OrderCommand();
        command.setType(OrderCommandType.APPROVE_ORDER);
        command.setCreatedAt(LocalDateTime.now());
        command.setOrderId(shipment.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleCommand(@Payload OrderCommand orderCommand) {
        if (orderCommand.getType().equals(OrderCommandType.APPROVE_ORDER)) {
            orderService.approveOrder(orderCommand.getOrderId());
        }
    }
}
