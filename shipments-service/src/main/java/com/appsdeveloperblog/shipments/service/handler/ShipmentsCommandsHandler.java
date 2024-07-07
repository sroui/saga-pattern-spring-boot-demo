package com.appsdeveloperblog.shipments.service.handler;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.core.dto.commands.CreateShipmentTicketCommand;
import com.appsdeveloperblog.core.dto.events.ShipmentTicketCreatedEvent;
import com.appsdeveloperblog.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${shipments.commands.topic.name}")
public class ShipmentsCommandsHandler {
    private final ShipmentService shipmentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String shipmentsEventsTopicName;

    public ShipmentsCommandsHandler(ShipmentService shipmentService,
                                    KafkaTemplate<String, Object> kafkaTemplate,
                                    @Value("${shipments.events.topic.name}") String shipmentsEventsTopicName) {
        this.shipmentService = shipmentService;
        this.kafkaTemplate = kafkaTemplate;
        this.shipmentsEventsTopicName = shipmentsEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload CreateShipmentTicketCommand command) {
        var shipment = new Shipment(command.getOrderId(), command.getPaymentId());
        shipmentService.createTicket(shipment);
        var shipmentTicketCreatedEvent = new ShipmentTicketCreatedEvent(shipment.getOrderId());
        kafkaTemplate.send(shipmentsEventsTopicName, shipmentTicketCreatedEvent);
    }
}
