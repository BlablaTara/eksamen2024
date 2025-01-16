package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DroneControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DroneRepository droneRepository;

    Station station = new Station();

    @BeforeEach
    void setUp() {
        //Station station = new Station();
        station.setStationId(1L);
        station.setDrones(new ArrayList<>());
        stationRepository.saveAndFlush(station);
    }

    //Possitiv test
    @Test
    void testAddDroneSuccess() throws Exception {

        mockMvc.perform(post("/api/drones/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialUuid").exists())
                .andExpect(jsonPath("$.droneStatus").value("I_DRIFT"));

    }

}
