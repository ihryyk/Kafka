package com.consumer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleSignal {

    @NotNull
    private String vehicleId;
    private double latitude;
    private double longitude;

}
