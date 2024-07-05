package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.core.dto.commands.*;
import com.appsdeveloperblog.core.dto.events.*;
import com.appsdeveloperblog.orders.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link CreateOrderSaga}
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreateOrderSagaTest {
    public static final String DOCKER_COMPOSE_FILE_PATH = "src/test/resources/docker/docker-compose.yml";
    @SpyBean
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;
    @Value("${shipments.commands.topic.name}")
    private String shipmentsCommandsTopicName;
    @Value("${orders.commands.topic.name}")
    private String ordersCommandsTopicName;
    @Value("${payments.events.topic.name}")
    private String paymentsEventsTopicName;
    @Value("${products.events.topic.name}")
    private String productsEventsTopicName;
    @Value("${shipments.events.topic.name}")
    private String shipmentsEventsTopicName;
    @SpyBean
    private CreateOrderSaga createOrderSaga;
    @Autowired
    private OrderService orderService;
    private final UUID productId = UUID.randomUUID();
    private final UUID paymentId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_FILE_PATH))
            .withExposedService("kafka-1", 9091)
            .withExposedService("kafka-2", 9092)
            .withExposedService("kafka-3", 9093);

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
    }

    @Test
    public void testSagaHappyPath() {
        mockOrderServicePlacesOrder();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(OrderCreatedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(productsCommandsTopicName), any(ReserveProductCommand.class));

        mockProductServiceSendsProductReservedEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(ProductReservedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(paymentsCommandsTopicName), any(ProcessPaymentCommand.class));

        mockPaymentServiceSendsPaymentProcessedEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(PaymentProcessedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(shipmentsCommandsTopicName), any(CreateShipmentTicketCommand.class));

        mockShipmentServiceSendsShipmentTicketCreatedEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(ShipmentTicketCreatedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(ordersCommandsTopicName), any(ApproveOrderCommand.class));
    }

    @Test
    public void testSagaUnhappyPath() {
        mockOrderServicePlacesOrder();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(OrderCreatedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(productsCommandsTopicName), any(ReserveProductCommand.class));

        mockProductServiceSendsProductReservedEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(ProductReservedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(paymentsCommandsTopicName), any(ProcessPaymentCommand.class));

        mockPaymentServiceSendsPaymentFailedEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(PaymentFailedEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(productsCommandsTopicName), any(CancelProductReservationCommand.class));
        verify(createOrderSaga, timeout(15_000).times(0)).handleEvent(any(PaymentProcessedEvent.class));

        mockProductServiceSendsProductReservationCanceledEvent();

        verify(createOrderSaga, timeout(15_000).times(1)).handleEvent(any(ProductReservationCanceledEvent.class));
        verify(kafkaTemplate, timeout(15_000).times(1)).send(eq(ordersCommandsTopicName), any(RejectOrderCommand.class));
    }

    private void mockProductServiceSendsProductReservationCanceledEvent() {
        var event = new ProductReservationCanceledEvent();
        event.setOrderId(orderId);
        event.setProductId(productId);
        kafkaTemplate.send(productsEventsTopicName, event);
    }

    private void mockPaymentServiceSendsPaymentFailedEvent() {
        var event = new PaymentFailedEvent();
        event.setOrderId(orderId);
        event.setProductId(productId);
        event.setProductQuantity(2);
        kafkaTemplate.send(paymentsEventsTopicName, event);
    }


    private void mockShipmentServiceSendsShipmentTicketCreatedEvent() {
        var event = new ShipmentTicketCreatedEvent();
        event.setOrderId(orderId);
        kafkaTemplate.send(shipmentsEventsTopicName, event);
    }

    private void mockPaymentServiceSendsPaymentProcessedEvent() {
        var event = new PaymentProcessedEvent();
        event.setPaymentId(paymentId);
        event.setOrderId(orderId);
        kafkaTemplate.send(paymentsEventsTopicName, event);
    }

    private void mockProductServiceSendsProductReservedEvent() {
        var event = new ProductReservedEvent();
        event.setProductId(productId);
        event.setOrderId(orderId);
        event.setProductPrice(new BigDecimal("100.00"));
        event.setProductQuantity(2);
        kafkaTemplate.send(productsEventsTopicName, event);
    }

    private void mockOrderServicePlacesOrder() {
        var order = new Order();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setCustomerId(customerId);
        order.setProductQuantity(2);
        orderService.placeOrder(order);
    }
}