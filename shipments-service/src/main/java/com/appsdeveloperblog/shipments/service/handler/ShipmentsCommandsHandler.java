package com.appsdeveloperblog.shipments.service.handler;

import com.appsdeveloperblog.core.dto.ShipmentCommand;
import com.appsdeveloperblog.core.types.ShipmentCommandType;
import com.appsdeveloperblog.shipments.service.ShipmentService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${shipments.commands.topic.name}")
public class ShipmentsCommandsHandler {
    private final ShipmentService shipmentService;

    public ShipmentsCommandsHandler(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ShipmentCommand command) {
        if (command.getType().equals(ShipmentCommandType.CREATE_SHIPMENT_TICKET)) {
            shipmentService.createTicket(command.getShipment());
        }
    }
}
