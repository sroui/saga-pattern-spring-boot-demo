package com.appsdeveloperblog.shipments.service;

import com.appsdeveloperblog.core.dto.Shipment;

import java.util.List;

public interface ShipmentService {
    List<Shipment> findAll();

    boolean isValid(Shipment shipment);

    void createTicket(Shipment shipment);
}
