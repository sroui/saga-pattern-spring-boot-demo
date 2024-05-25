package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.Product;
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

import java.math.BigDecimal;

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
        var price = new BigDecimal(30); // we could communicate with a service to fetch product data
        var productName = "shampoo"; // we could communicate with a service to fetch product data
        var product = new Product(event.getProductId(), event.getOrderId(), event.getCustomerId(), productName, price);

        var command = new ReserveProductCommand(
                product.getId(), product.getOrderId(), product.getCustomerId(), product.getName(), product.getPrice());
        kafkaTemplate.send(productsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {
        var command = new ProcessPaymentCommand(
                event.getProductId(), event.getOrderId(), event.getCustomerId(), event.getProductPrice());
        kafkaTemplate.send(paymentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        CreateShipmentTicketCommand command =
                new CreateShipmentTicketCommand(event.getOrderId(), event.getCustomerId(), event.getProductId());
        kafkaTemplate.send(shipmentsCommandsTopicName, command);
    }

    @KafkaHandler
    public void handleEvent(@Payload ShipmentTicketCreatedEvent event) {
        ApproveOrderCommand command = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }
}
