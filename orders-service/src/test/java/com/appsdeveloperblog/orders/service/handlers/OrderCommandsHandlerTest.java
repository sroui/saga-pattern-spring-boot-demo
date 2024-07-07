package com.appsdeveloperblog.orders.service.handlers;

import com.appsdeveloperblog.core.dto.commands.ApproveOrderCommand;
import com.appsdeveloperblog.core.dto.commands.RejectOrderCommand;
import com.appsdeveloperblog.orders.service.OrderService;
import com.appsdeveloperblog.orders.utils.KafkaAwareIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
class OrderCommandsHandlerTest extends KafkaAwareIntegrationTest {
    @MockBean
    private OrderService orderService;
    @SpyBean
    private OrderCommandsHandler orderCommandsHandler;

    @Test
    void givenApproveOrderCommand_thenApproveOrder() {
        UUID orderId = UUID.randomUUID();
        mockSagaSendsOrderApprovedCommand(orderId);

        verify(orderService, timeout(15_000).times(1)).approveOrder(orderId);
        verify(orderCommandsHandler, timeout(15_000).times(1)).handleCommand(any(ApproveOrderCommand.class));
    }

    @Test
    void givenRejectOrderCommand_thenRejectOrder() {
        UUID orderId = UUID.randomUUID();
        mockSagaSendsRejectOrderCommand(orderId);

        verify(orderService, timeout(15_000).times(1)).rejectOrder(orderId);
        verify(orderCommandsHandler, timeout(15_000).times(1)).handleCommand(any(RejectOrderCommand.class));
    }

    private void mockSagaSendsOrderApprovedCommand(UUID orderId) {
        var command = new ApproveOrderCommand();
        command.setOrderId(orderId);
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }

    private void mockSagaSendsRejectOrderCommand(UUID orderId) {
        var command = new RejectOrderCommand();
        command.setOrderId(orderId);
        kafkaTemplate.send(ordersCommandsTopicName, command);
    }
}