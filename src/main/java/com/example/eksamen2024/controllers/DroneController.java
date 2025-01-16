package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneDTO;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<DroneDTO> getDrones() {
        return droneRepository.findAll().stream()
                .map(drone -> new DroneDTO(
                        drone.getSerialUuid(),
                        drone.getDroneStatus(),
                        drone.getStation() != null ? drone.getStation().getStationId() : null
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/drones/add")
    public ResponseEntity<Drone> addDrone() {
        List<Station> stations = stationRepository.findAll();

        if (stations.isEmpty()) {
            System.out.println("Ingen stationer i databasen!");
            return ResponseEntity.badRequest().body(null);
        }

        //Hvis der er 2 stationer med samme antal droner, vælger den, nbare den første som opfylder betingelserne.
        Station stationWithFewestDrones = stations.stream()
                .min(Comparator.comparingInt(station -> station.getDrones().size()))
                .orElseThrow(() -> new IllegalStateException("Fejl ved at finde station med færrest droner"));

        System.out.println("Station valgt: " + stationWithFewestDrones.getStationId());

        Drone newDrone = new Drone(
                UUID.randomUUID(),
                DroneStatus.I_DRIFT,
                stationWithFewestDrones,
                null
        );

        droneRepository.save(newDrone);
        System.out.println("Drone oprettet: " + newDrone);


        return ResponseEntity.ok(newDrone);
    }

}
