/*package com.example.eksamen2024.initData;

import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitStation implements CommandLineRunner {

    private final StationRepository stationRepository;

    public InitStation(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        createStation(12.34, 55.41);
        createStation(12.45, 55.67);
        createStation(12.25, 55.50);


        System.out.println("Stationer initialiseret.");
    }

    // Hjælpe metode til at oprette station
    private void createStation(double longitude, double latitude) {
        Station station = new Station();
        station.setLongitude(longitude);
        station.setLatitude(latitude);
        stationRepository.save(station);
    }
}

 */
