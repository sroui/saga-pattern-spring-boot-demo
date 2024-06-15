package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.commands.*;
import com.appsdeveloperblog.core.dto.events.*;
import com.appsdeveloperblog.core.types.OrderStatus;
import com.appsdeveloperblog.orders.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        "${orders.events.topic.name}",
        "${products.events.topic.name}",
        "${payments.events.topic.name}",
        "${shipments.events.topic.name}",
})
public class CreateOrderSaga {
    private final OrderHistoryService orderHistoryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productsCommandsTopicName;
    private final String paymentsCommandsTopicName;
    private final String shipmentsCommandsTopicName;
    private final String ordersCommandsTopicName;

    public CreateOrderSaga(OrderHistoryService orderHistoryService,
                           KafkaTemplate<String, Object> kafkaTemplate,
                           @Value("${products.commands.topic.name}") String productsCommandsTopicName,
                           @Value("${payments.commands.topic.name}") String paymentsCommandsTopicName,
                           @Value("${shipments.commands.topic.name}") String shipmentsCommandsTopicName,
                           @Value("${orders.commands.topic.name}") String ordersCommandsTopicName) {
        this.orderHistoryService = orderHistoryService;
        this.kafkaTemplate = kafkaTemplate;
        this.productsCommandsTopicName = productsCommandsTopicName;
        this.paymentsCommandsTopicName = paymentsCommandsTopicName;
        this.shipmentsCommandsTopicName = shipmentsCommandsTopicName;
        this.ordersCommandsTopicName = ordersCommandsTopicName;
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {
        var command = new ReserveProductCommand(event.getOrderId(), event.getProductId(), event.getProductQuantity());
        kafkaTemplate.send(productsCommandsTopicName, command);
        orderHistoryService.add(event.getOrderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent event) {
        orderHistoryService.add(event.getOrderId(), OrderStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {
        var command = new ProcessPaymentCommand(
                event.getOrderId(), event.getProductId(), event.getProductPrice(), event.getProductQuantity());
        kafkaTemplate.send(paymentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCanceledEvent event) {
        var command = new RejectOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, command);
        orderHistoryService.add(event.getOrderId(), OrderStatus.REJECTED);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        var command = new CreateShipmentTicketCommand(event.getOrderId(), event.getPaymentId());
        kafkaTemplate.send(shipmentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent event) {
        var command = new CancelProductReservationCommand(event.getOrderId(), event.getProductId(), event.getProductQuantity());
        kafkaTemplate.send(productsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ShipmentTicketCreatedEvent event) {
        var command = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }
}
