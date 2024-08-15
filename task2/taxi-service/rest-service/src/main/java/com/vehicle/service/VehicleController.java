package com.vehicle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleProducerService vehicleProducerService;

    @PostMapping
    void saveSignal(@RequestBody VehicleSignal vehicleSignal){
        vehicleProducerService.saveVehicle(vehicleSignal);
    }
}
