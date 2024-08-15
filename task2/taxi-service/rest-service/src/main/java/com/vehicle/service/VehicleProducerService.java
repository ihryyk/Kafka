package com.vehicle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void saveVehicle(VehicleSignal vehicleSignal) {
        try {
            String vehicleSignalJson = objectMapper.writeValueAsString(vehicleSignal);
            kafkaTemplate.send("input", vehicleSignalJson);
        } catch (JsonProcessingException exp) {
            throw new IllegalArgumentException("Failed to convert to JSON.", exp);
        }
    }

}
