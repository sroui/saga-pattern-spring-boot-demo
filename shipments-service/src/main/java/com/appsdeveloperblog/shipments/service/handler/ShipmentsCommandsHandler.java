package com.appsdeveloperblog.shipments.service.handler;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.core.dto.commands.CreateShipmentTicketCommand;
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
    public void handleCommand(@Payload CreateShipmentTicketCommand command) {
        var shipment = new Shipment(null, command.getOrderId(), command.getProductId(), command.getCustomerId());
        shipmentService.createTicket(shipment);
    }
}
