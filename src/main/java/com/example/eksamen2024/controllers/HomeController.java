/*package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.repositories.DeliveryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
@RestController
public class HomeController {

    private final DeliveryRepository deliveryRepository;

    public HomeController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping("/api/deliveries")
    public List<Delivery> getDeliveries() {
        return deliveryRepository.findAll();
    }
}

 */
