package com.appsdeveloperblog.shipments.service;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.shipments.jpa.entity.ShipmentEntity;
import com.appsdeveloperblog.shipments.jpa.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void createTicket(Shipment shipment) {
        if (isValid(shipment)) {
            ShipmentEntity shipmentEntity = new ShipmentEntity();
            shipmentEntity.setPaymentId(shipment.getPaymentId());
            shipmentEntity.setOrderId(shipment.getOrderId());
            shipmentRepository.save(shipmentEntity);
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
