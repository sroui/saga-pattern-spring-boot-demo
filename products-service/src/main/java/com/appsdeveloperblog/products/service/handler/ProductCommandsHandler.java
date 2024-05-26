package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.products.service.ProductService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
public class ProductCommandsHandler {
    private final ProductService productService;

    public ProductCommandsHandler(ProductService productService) {
        this.productService = productService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        Product product = new Product(command.getProductId(), command.getProductQuantity());
        productService.reserve(product, command.getOrderId());
    }
}
