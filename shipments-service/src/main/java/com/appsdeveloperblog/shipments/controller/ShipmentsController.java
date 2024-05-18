package com.appsdeveloperblog.shipments.controller;

import com.appsdeveloperblog.core.dto.Shipment;
import com.appsdeveloperblog.shipments.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentsController {
    private final ShipmentService shipmentService;

    public ShipmentsController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Shipment> findAll() {
        return shipmentService.findAll();
    }
}
