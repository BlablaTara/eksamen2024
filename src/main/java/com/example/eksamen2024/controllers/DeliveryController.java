package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.models.Pizza;
import com.example.eksamen2024.repositories.DeliveryRepository;
import com.example.eksamen2024.repositories.PizzaRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
    @RestController
    public class DeliveryController {

        private final DeliveryRepository deliveryRepository;
        private final PizzaRepository pizzaRepository;
        private final Random random = new Random();

        // Random pizzaer der bliver tildelt i /deliveries/add
        private final List<String> pizzaNames = Arrays.asList("Margherita", "Pepperoni", "Hawaii", "Vegetariana", "Trés Fromage");
        private final List<Integer> pizzaPrices = Arrays.asList(85, 70, 65);

        //Og Random Adresser der bliver tildelt og i /deliveries/add
        private final List<String> addresses = Arrays.asList(
                "Pizza Street 1234",
                "Kastrupvej 56",
                "Saxogade 24",
                "Guldberggade 13",
                "Blågårds Plads 9"
        );

        public DeliveryController(DeliveryRepository deliveryRepository, PizzaRepository pizzaRepository) {
            this.deliveryRepository = deliveryRepository;
            this.pizzaRepository = pizzaRepository;
        }

        @GetMapping("/api/deliveries")
        public ResponseEntity<List<Delivery>> getDeliveries() {
            List<Delivery> deliveries = deliveryRepository.findAll().stream()
                    .filter(delivery -> delivery.getActualDeliveryTime() == null)
                    .collect(Collectors.toList());

            if (deliveries.isEmpty()) {
                throw new IllegalArgumentException("Ingen leveringer fundet."); // Kaster en exception hvis der ikke findes leveringer
            }

            return ResponseEntity.ok(deliveries);
        }

        @PostMapping("/api/deliveries/add")
        public ResponseEntity<Delivery> addDelivery() throws BadRequestException {
            try {

                // Vælger en random pizza fra listen
                String pizzaName = pizzaNames.get(random.nextInt(pizzaNames.size()));
                int pizzaPrice = pizzaPrices.get(random.nextInt(pizzaPrices.size()));

                Pizza pizza = new Pizza();
                pizza.setTitel(pizzaName);
                pizza.setPrice(pizzaPrice);

                pizzaRepository.save(pizza);

                // Vælg en random adresse fra listen
                String address = addresses.get(random.nextInt(addresses.size()));

                Delivery delivery = new Delivery();
                delivery.setAddress(address);
                delivery.setPizza(pizza);
                delivery.setExpectedDeliveryTime(LocalDateTime.now().plusMinutes(30));
                delivery.setActualDeliveryTime(null);

                deliveryRepository.save(delivery);

                return ResponseEntity.status(201).body(delivery);
            } catch (Exception e) {
                throw new BadRequestException("Fejl ved oprettelse af levering: " + e.getMessage());
            }
        }
    }







