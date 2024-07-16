package com.appsdeveloperblog.products.utils;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.util.UUID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class KafkaAwareIntegrationTest {
    public static final String DOCKER_COMPOSE_FILE_PATH = "src/test/resources/docker/docker-compose.yml";

    public static DockerComposeContainer<?> environment = new DockerComposeContainer<>(new File(DOCKER_COMPOSE_FILE_PATH))
            .withExposedService("kafka-1", 9291)
            .withExposedService("kafka-2", 9292)
            .withExposedService("kafka-3", 9293);

    @SpyBean
    protected KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${products.commands.topic.name}")
    protected String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    protected String paymentsCommandsTopicName;
    @Value("${shipments.commands.topic.name}")
    protected String shipmentsCommandsTopicName;
    @Value("${orders.commands.topic.name}")
    protected String ordersCommandsTopicName;
    @Value("${payments.events.topic.name}")
    protected String paymentsEventsTopicName;
    @Value("${products.events.topic.name}")
    protected String productsEventsTopicName;
    @Value("${shipments.events.topic.name}")
    protected String shipmentsEventsTopicName;

    @BeforeAll
    static void setUp() {
        environment.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        UUID uuid = UUID.randomUUID();
        registry.add("orders.events.topic.name", () -> "orders-events-" + uuid);
        registry.add("orders.commands.topic.name", () -> "orders-commands-" + uuid);
        registry.add("products.events.topic.name", () -> "products-events-" + uuid);
        registry.add("products.commands.topic.name", () -> "products-commands-" + uuid);
        registry.add("payments.events.topic.name", () -> "payments-events-" + uuid);
        registry.add("payments.commands.topic.name", () -> "payments-commands-" + uuid);
        registry.add("shipments.events.topic.name", () -> "shipments-events-" + uuid);
        registry.add("shipments.commands.topic.name", () -> "shipments-commands-" + uuid);
        registry.add("spring.kafka.admin.auto-create", () -> "true");
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9291,localhost:9292,localhost:9293");
    }
}
