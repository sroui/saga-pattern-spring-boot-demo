package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.core.dto.commands.*;
import com.appsdeveloperblog.core.dto.events.*;
import com.appsdeveloperblog.orders.service.OrderService;
import com.appsdeveloperblog.orders.utils.KafkaAwareIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link CreateOrderSaga}
 */
@SpringBootTest
class CreateOrderSagaTest extends KafkaAwareIntegrationTest {
    @SpyBean
    private CreateOrderSaga createOrderSaga;
    @Autowired
    private OrderService orderService;
    private final UUID productId = UUID.randomUUID();
    private final UUID paymentId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

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