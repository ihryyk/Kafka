package com.vehicle.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;


@Data
@AllArgsConstructor
public class VehicleSignal {

    @NotNull
    private String vehicleId;
    private double latitude;
    private double longitude;

}
