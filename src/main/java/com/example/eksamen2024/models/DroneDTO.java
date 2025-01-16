package com.example.eksamen2024.models;

import java.util.UUID;

public record DroneDTO(UUID serialUuid, DroneStatus droneStatus, Long stationId) {
}
