package com.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleConsumerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CalculateDistanceFunction calculateDistanceFunction;
    private final ObjectMapper objectMapper;
    private final Map<String, VehicleSignal> lastSignalPerVehicle = new HashMap<>();
    private final Map<String, Double> distancePerVehicle = new HashMap<>();

    @KafkaListener(topics = "input", groupId = "group-id", concurrency = "3")
    public void consume(String message) {
        try {
            log.info("Received signal: {} ...", message);
            ObjectMapper mapper = new ObjectMapper();
            VehicleSignal vehicleSignal = mapper.readValue(message, VehicleSignal.class);
            calculateDistance(vehicleSignal);
            kafkaTemplate.send("output",
                    vehicleSignal.getVehicleId(),
                    String.valueOf(distancePerVehicle.get(vehicleSignal.getVehicleId())));
        } catch (JsonProcessingException exp) {
            throw new IllegalArgumentException("Failed to convert to JSON.", exp);
        }
    }

    private void calculateDistance(VehicleSignal signal) {
        VehicleSignal lastSignal = lastSignalPerVehicle.get(signal.getVehicleId());
        if (lastSignal != null) {
            double distance = calculateDistanceFunction.apply(lastSignal, signal);
            distancePerVehicle.put(signal.getVehicleId(), distancePerVehicle.getOrDefault(signal.getVehicleId(), 0D) + distance);
            log.info("Calculated distance: {} km .", distance);
        }
        lastSignalPerVehicle.put(signal.getVehicleId(), signal);
    }

}
