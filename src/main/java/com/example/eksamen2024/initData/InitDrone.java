package com.example.eksamen2024.initData;

import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InitDrone implements CommandLineRunner {

    private final DroneRepository droneRepository;
    private final StationRepository stationRepository;

    public InitDrone(DroneRepository droneRepository, StationRepository stationRepository) {
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Station> optionalStation = stationRepository.findById(1L);

        if (optionalStation.isPresent()) {
            Station station = optionalStation.get();

            createDrone(station);
        } else {
            System.err.println("No station found with ID 1. Cannot initialize drone.");
        }
    }

    private void createDrone(Station station) {
        Drone drone = new Drone(
                UUID.randomUUID(),            // Generer en unik UUID for dronen
                DroneStatus.I_DRIFT,          // Driftstatus
                station,                      // Tildel stationen
                null                          // Ingen leverancer endnu
        );

        // Gem dronen i databasen
        droneRepository.save(drone);

        System.out.println("Drone initialized with serial UUID: " + drone.getSerialUuid());
    }
}
