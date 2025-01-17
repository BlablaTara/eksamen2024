package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneControllerUnitTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private DroneController droneController;

    public DroneControllerUnitTest() {
        MockitoAnnotations.openMocks(this);
    }

    //Negativ test.
    @Test
    void testAddDroneFaile() {
        // Arrange: Ingen stationer i databasen
        when(stationRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = droneController.addDrone();

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Ingen stationer i databasen!", response.getBody());
    }

}
