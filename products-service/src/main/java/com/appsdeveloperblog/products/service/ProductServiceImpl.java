package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.products.dao.jpa.entity.ProductEntity;
import com.appsdeveloperblog.products.dao.jpa.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
    public void reserve(Product product) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        productRepository.save(productEntity);
        product.setId(productEntity.getId());

        kafkaTemplate.send(productEventsTopicName, product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(entity -> {
            Product product = new Product();
            BeanUtils.copyProperties(entity, product);
            return product;
        }).collect(Collectors.toList());
    }
}
