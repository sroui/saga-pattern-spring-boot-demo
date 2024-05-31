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
        boolean enoughQuantity = productEntity.getQuantity() >= product.getQuantity();
        Assert.isTrue(enoughQuantity, "Not enough amount to reserve product " + product.getId());
        productEntity.setQuantity(productEntity.getQuantity() - product.getQuantity());
        productRepository.save(productEntity);

        var productReservedEvent =
                new ProductReservedEvent(orderId,
                        productEntity.getId(),
                        productEntity.getPrice(),
                        product.getQuantity());
        kafkaTemplate.send(productEventsTopicName, productReservedEvent);
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());
        productEntity.setQuantity(product.getQuantity());
        productRepository.save(productEntity);

        return new Product(productEntity.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity()))
                .collect(Collectors.toList());
    }
}
