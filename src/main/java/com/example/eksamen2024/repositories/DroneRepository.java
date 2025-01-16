package com.example.eksamen2024.repositories;

import com.example.eksamen2024.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    boolean existsBySerialUuid(UUID serialUuid);

}
