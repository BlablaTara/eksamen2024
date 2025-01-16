package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneDTO;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
@RestController
public class DroneController {

    private final DroneRepository droneRepository;
    private final StationRepository stationRepository;

    public DroneController(DroneRepository droneRepository, StationRepository stationRepository) {
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
    }

    @GetMapping("/api/drones")
    public ResponseEntity<List<DroneDTO>> getDrones() {
        try {
            List<DroneDTO> drones = droneRepository.findAll().stream()
                    .map(drone -> new DroneDTO(
                            drone.getSerialUuid(),
                            drone.getDroneStatus(),
                            drone.getStation() != null ? drone.getStation().getStationId() : null
                    ))
                    .collect(Collectors.toList());

            if (drones.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content hvis der ikke er droner
            }

            return ResponseEntity.ok(drones);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping("/api/drones/add")
    public ResponseEntity<?> addDrone() {
        List<Station> stations = stationRepository.findAll();

        if (stations.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingen stationer i databasen!");
        }

        // Hvis der er 2 stationer med samme antal droner, vælger den den første, som opfylder betingelserne.
        Station stationWithFewestDrones = stations.stream()
                .min(Comparator.comparingInt(station -> station.getDrones().size()))
                .orElseThrow(() -> new IllegalStateException("Fejl ved at finde station med færrest droner"));

        Drone newDrone = new Drone(
                UUID.randomUUID(),
                DroneStatus.I_DRIFT,
                stationWithFewestDrones,
                null
        );

        droneRepository.save(newDrone);
        return ResponseEntity.status(201).body(newDrone); // 201 Created
    }

    @PostMapping("/api/drones/enable/{droneId}")
    public ResponseEntity<Drone> enableDrone(@PathVariable Long droneId) {
        // Find dronen med det givet ID
        return droneRepository.findById(droneId)
                .map(drone -> {
                    // Skift status til "i drift"
                    drone.setDroneStatus(DroneStatus.I_DRIFT);
                    droneRepository.save(drone); // Gem ændringen i databasen
                    return ResponseEntity.ok(drone); // Returner den opdaterede drone
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/drones/disable/{droneId}")
    public ResponseEntity<Drone> disableDrone(@PathVariable Long droneId) {
        // Find dronen med det givet ID
        return droneRepository.findById(droneId)
                .map(drone -> {
                    // Skift status til "ude af drift"
                    drone.setDroneStatus(DroneStatus.UDE_AF_DRIFT);
                    droneRepository.save(drone); // Gem ændringen i databasen
                    return ResponseEntity.ok(drone); // Returner den opdaterede drone
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Hvis dronen ikke findes, returnér 404
    }
}





