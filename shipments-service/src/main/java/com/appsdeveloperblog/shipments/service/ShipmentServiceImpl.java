package com.appsdeveloperblog.shipments.service;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.core.dto.events.ShipmentTicketCreatedEvent;
import com.appsdeveloperblog.shipments.jpa.entity.ShipmentEntity;
import com.appsdeveloperblog.shipments.jpa.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String shipmentsEventsTopicName;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository,
                               KafkaTemplate<String, Object> kafkaTemplate,
                               @Value("${shipments.events.topic.name}") String shipmentsEventsTopicName) {
        this.shipmentRepository = shipmentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.shipmentsEventsTopicName = shipmentsEventsTopicName;
    }

    @Override
    public void createTicket(Shipment shipment) {
        if (isValid(shipment)) {
            ShipmentEntity shipmentEntity = new ShipmentEntity();
            shipmentEntity.setPaymentId(shipment.getPaymentId());
            shipmentEntity.setOrderId(shipment.getOrderId());
            shipmentRepository.save(shipmentEntity);

            var shipmentTicketCreatedEvent = new ShipmentTicketCreatedEvent(shipment.getOrderId());
            kafkaTemplate.send(shipmentsEventsTopicName, shipmentTicketCreatedEvent);
        } else {
            // todo
        }
    }

    @Override
    public boolean isValid(Shipment shipment) {
        return true; //todo
    }

    @Override
    public List<Shipment> findAll() {
        return shipmentRepository.findAll().stream()
                .map(entity -> new Shipment(entity.getId(), entity.getOrderId(), entity.getPaymentId()))
                .collect(Collectors.toList());
    }
}
