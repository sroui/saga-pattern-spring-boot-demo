package com.appsdeveloperblog.shipments.service.handler;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.core.dto.commands.CreateShipmentTicketCommand;
import com.appsdeveloperblog.core.dto.events.ShipmentTicketCreatedEvent;
import com.appsdeveloperblog.shipments.service.ShipmentService;
import com.appsdeveloperblog.shipments.utils.KafkaAwareIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ShipmentsCommandsHandlerTest extends KafkaAwareIntegrationTest {
    @SpyBean
    private ShipmentService shipmentService;
    @SpyBean
    private ShipmentsCommandsHandler shipmentsCommandsHandler;

    @Test
    void givenCreateShipmentTicketCommand_whenTicketCreated_thenSendShipmentTicketCreatedEvent() {
        doAnswer(inv -> inv).when(shipmentService).createTicket(any());

        mockSagaSendsCreateShipmentTicketCommand();

        verify(shipmentsCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(CreateShipmentTicketCommand.class));
        verify(shipmentService, timeout(15_000).times(1)).createTicket(any(Shipment.class));
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(shipmentsEventsTopicName), any(ShipmentTicketCreatedEvent.class));
    }

    private void mockSagaSendsCreateShipmentTicketCommand() {
        var command = new CreateShipmentTicketCommand();
        command.setOrderId(UUID.randomUUID());
        command.setPaymentId(UUID.randomUUID());
        kafkaTemplate.send(shipmentsCommandsTopicName, command);
    }
}