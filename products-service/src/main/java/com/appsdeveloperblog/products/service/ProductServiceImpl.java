package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.products.dao.jpa.entity.ProductEntity;
import com.appsdeveloperblog.products.dao.jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;

    public ProductServiceImpl(ProductRepository productRepository,
                              KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${products.events.topic.name}") String productEventsTopicName) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @Override
    public void reserve(Product product, Long orderId) {
        ProductEntity productEntity = productRepository.findById(product.getId()).orElseThrow();
        Assert.isTrue(productEntity.getQuantity() - 1 >= 0, "Not enough products to reserve");
        productEntity.setQuantity(productEntity.getQuantity() - 1);
        productRepository.save(productEntity);

        var productReservedEvent =
                new ProductReservedEvent(orderId,
                        product.getId(),
                        product.getPrice(),
                        product.getQuantity());
        kafkaTemplate.send(productEventsTopicName, productReservedEvent);
    }

    @Override
    public void save(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());
        productEntity.setQuantity(product.getQuantity());
        productRepository.save(productEntity);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity()))
                .collect(Collectors.toList());
    }
}
