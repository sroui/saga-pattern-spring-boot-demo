package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.core.dto.ProductCommand;
import com.appsdeveloperblog.core.types.ProductCommandType;
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
    public void handleCommand(@Payload ProductCommand command) {
        if (command.getType().equals(ProductCommandType.RESERVE_PRODUCT)) {
            productService.reserve(command.getProduct());
        }
    }
}
