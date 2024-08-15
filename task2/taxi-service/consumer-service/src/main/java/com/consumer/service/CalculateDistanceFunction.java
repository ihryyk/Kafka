package com.consumer.service;

import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class CalculateDistanceFunction implements BiFunction<VehicleSignal, VehicleSignal, Double> {

    @Override
    public Double apply(VehicleSignal s1, VehicleSignal s2) {
        return DistanceUtils.distHaversineRAD(
                Math.toRadians(s1.getLatitude()), Math.toRadians(s1.getLongitude()),
                Math.toRadians(s2.getLatitude()), Math.toRadians(s2.getLongitude())
        );
    }

}
