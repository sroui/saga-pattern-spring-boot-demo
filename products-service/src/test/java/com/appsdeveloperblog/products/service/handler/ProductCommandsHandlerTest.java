package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.core.dto.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.ProductReservationCanceledEvent;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.core.exceptions.ProductInsufficientQuantityException;
import com.appsdeveloperblog.products.service.ProductService;
import com.appsdeveloperblog.products.utils.KafkaAwareIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductCommandsHandlerTest extends KafkaAwareIntegrationTest {
    @MockBean
    private ProductService productService;
    @SpyBean
    private ProductCommandsHandler productCommandsHandler;

    @Test
    void givenReserveProductCommand_whenProductReservationSucceeded_thenSendProductReservedEvent() {
        when(productService.reserve(any(), any())).thenReturn(mock(Product.class));

        mockSagaSendsReserveProductCommand();

        verify(productCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(ReserveProductCommand.class));
        verify(productService, timeout(15_000).times(1)).reserve(any(Product.class), any());
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(productsEventsTopicName), any(ProductReservedEvent.class));
    }

    @Test
    void givenReserveProductCommand_whenProductReservationFailed_thenSendProductReservationCanceledEvent() {
        when(productService.reserve(any(), any())).thenThrow(mock(ProductInsufficientQuantityException.class));

        mockSagaSendsReserveProductCommand();

        verify(productCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(ReserveProductCommand.class));
        verify(productService, timeout(15_000).times(1)).reserve(any(Product.class), any());
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(productsEventsTopicName), any(ProductReservationCanceledEvent.class));
    }


    @Test
    void givenCancelProductReservationCommand_whenReservationCanceled_thenSendProductReservationCanceledEvent() {
        doAnswer(inv -> inv).when(productService).cancelReservation(any(), any());

        mockSagaSendsCancelProductReservationCommand();

        verify(productCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(CancelProductReservationCommand.class));
        verify(productService, timeout(15_000).times(1)).cancelReservation(any(Product.class), any());
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(productsEventsTopicName), any(ProductReservationCanceledEvent.class));
    }

    private void mockSagaSendsReserveProductCommand() {
        var command = new ReserveProductCommand();
        command.setOrderId(UUID.randomUUID());
        command.setProductId(UUID.randomUUID());
        command.setProductQuantity(2);
        kafkaTemplate.send(productsCommandsTopicName, command);
    }

    private void mockSagaSendsCancelProductReservationCommand() {
        var command = new CancelProductReservationCommand();
        command.setOrderId(UUID.randomUUID());
        command.setProductId(UUID.randomUUID());
        command.setProductQuantity(2);
        kafkaTemplate.send(productsCommandsTopicName, command);
    }
}