package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.core.dto.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.ProductReservationCanceledEvent;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.core.exceptions.ProductInsufficientQuantityException;
import com.appsdeveloperblog.products.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
public class ProductCommandsHandler {
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;

    public ProductCommandsHandler(ProductService productService,
                                  KafkaTemplate<String, Object> kafkaTemplate,
                                  @Value("${products.events.topic.name}") String productEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
            var reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
            var productReservedEvent = new ProductReservedEvent(command.getOrderId(),
                    reservedProduct.getId(),
                    reservedProduct.getPrice(),
                    reservedProduct.getQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (ProductInsufficientQuantityException e) {
            var productReservationCanceledEvent =
                    new ProductReservationCanceledEvent(command.getOrderId(), command.getProductId());
            kafkaTemplate.send(productEventsTopicName, productReservationCanceledEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(@Payload CancelProductReservationCommand command) {
        Product undesiredProduct = new Product(command.getProductId(), command.getProductQuantity());
        productService.cancelReservation(undesiredProduct, command.getOrderId());
        var productReservationCanceledEvent =
                new ProductReservationCanceledEvent(command.getOrderId(), command.getProductId());
        kafkaTemplate.send(productEventsTopicName, productReservationCanceledEvent);
    }
}
