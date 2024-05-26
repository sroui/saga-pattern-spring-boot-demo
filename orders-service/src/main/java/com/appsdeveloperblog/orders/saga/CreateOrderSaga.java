package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.commands.ApproveOrderCommand;
import com.appsdeveloperblog.core.dto.commands.CreateShipmentTicketCommand;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.OrderCreatedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.core.dto.events.ShipmentTicketCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;
    @Value("${shipments.commands.topic.name}")
    private String shipmentsCommandsTopicName;
    @Value("${orders.commands.topic.name}")
    private String ordersCommandsTopicName;

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {
        var command = new ReserveProductCommand(event.getOrderId(), event.getProductId(), event.getProductQuantity());
        kafkaTemplate.send(productsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {
        var command = new ProcessPaymentCommand(
                event.getOrderId(), event.getProductId(), event.getProductPrice(), event.getProductQuantity());
        kafkaTemplate.send(paymentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        CreateShipmentTicketCommand command = new CreateShipmentTicketCommand(event.getOrderId() ,event.getPaymentId());
        kafkaTemplate.send(shipmentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ShipmentTicketCreatedEvent event) {
        ApproveOrderCommand command = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }
}
