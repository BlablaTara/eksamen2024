package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.repositories.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeliveryControllerUnitTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryController deliveryController;

    public DeliveryControllerUnitTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFinishDeliveryNoDrone() {
        Long deliveryId = 1L;
        // Opret en levering uden drone
        Delivery delivery = new Delivery(deliveryId, null, LocalDateTime.now().minusMinutes(10), null);

        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

        ResponseEntity<?> response = deliveryController.finishDelivery(deliveryId);

        assertEquals(400, response.getStatusCodeValue());  // 400 for Bad Request
        assertTrue(response.getBody().toString().contains("Fejl: Leveringen mangler en drone og kan ikke markeres som færdig."));

        // Verificer, at findById blev kaldt én gang
        verify(deliveryRepository, times(1)).findById(deliveryId);
    }
}