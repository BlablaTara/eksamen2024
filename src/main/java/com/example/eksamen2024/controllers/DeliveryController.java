package com.example.eksamen2024.controllers;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Pizza;
import com.example.eksamen2024.repositories.DeliveryRepository;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.PizzaRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
@RestController
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final PizzaRepository pizzaRepository;
    private final DroneRepository droneRepository;
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

    public DeliveryController(DeliveryRepository deliveryRepository, PizzaRepository pizzaRepository, DroneRepository droneRepository) {
        this.deliveryRepository = deliveryRepository;
        this.pizzaRepository = pizzaRepository;
        this.droneRepository = droneRepository;

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

    @GetMapping("/api/deliveries/queue")
    public ResponseEntity<List<Delivery>> getDeliveryQueue() {
        try {
            List<Delivery> deliveriesWithoutDrone = deliveryRepository.findByDroneIsNull();

            //Retunerer en tom liste, hvis ikke der er oprettet nogen deliveries
            if (deliveriesWithoutDrone.isEmpty()) {
                return ResponseEntity.status(200).body(deliveriesWithoutDrone);
            }
            return ResponseEntity.status(200).body(deliveriesWithoutDrone);
        } catch (Exception e) {
            throw new RuntimeException("Fejl ved hentning af leveringskø: " + e.getMessage());
        }
    }

    @PostMapping("/api/deliveries/schedule")
    public ResponseEntity<?> scheduleDelivery(@RequestBody Map<String, Long> requestBody) throws BadRequestException {
        Long droneId = requestBody.get("droneId");
        try {
            List<Delivery> deliveriesWithoutDrone = deliveryRepository.findByDroneIsNull();
            if (deliveriesWithoutDrone.isEmpty()) {
                throw new BadRequestException("Der er ingen leveringer, der mangler en drone.");
            }

            Delivery deliveryToSchedule = deliveriesWithoutDrone.get(0);

            // Kan vælge at tildele en drone på
            // http://localhost:8080/api/deliveries/schedule?droneId=1
            // Ellers finder den selv en drone der er i drift.

            Drone drone;
            if (droneId != null) {
                drone = droneRepository.findById(droneId)
                        .orElseThrow(() -> new BadRequestException("Drone ikke fundet."));
            } else {
                // Ellers vælg en tilfældig drone, der er i drift og ikke tildelt en levering
                drone = droneRepository.findAll().stream()
                        .filter(d -> d.getDroneStatus() == DroneStatus.I_DRIFT)
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException("Der er ingen tilgængelige droner i drift."));
            }

            if (drone.getDroneStatus() == DroneStatus.UDE_AF_DRIFT || drone.getDroneStatus() == DroneStatus.UDFASET) {
                throw new BadRequestException("Dronen er: (" + drone.getDroneStatus() + ") og kan ikke tildeles leveringen.");
            }

            if (deliveryToSchedule.getDrone() != null) {
                throw new BadRequestException("Leveringen har allerede en drone.");
            }

            // Tildeler, opdaterer og gemmer dronen på leveringen
            deliveryToSchedule.setDrone(drone);
            deliveryRepository.save(deliveryToSchedule);

            return ResponseEntity.status(HttpStatus.OK).body(deliveryToSchedule);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fejl: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fejl under planlægning af levering: " + ex.getMessage());
        }
    }

    @PostMapping("/api/deliveries/finish")
    public ResponseEntity<?> finishDelivery(@RequestParam("deliveryId") Long deliveryId) {
        try {
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new BadRequestException("Levering med ID " + deliveryId + " ikke fundet."));

            if (delivery.getDrone() == null) {
                throw new BadRequestException("Leveringen mangler en drone og kan ikke markeres som færdig.");
            }

    // Angiv den aktuelle leveringstid
            delivery.setActualDeliveryTime(LocalDateTime.now());
            deliveryRepository.save(delivery);

            return ResponseEntity.status(HttpStatus.OK).body(delivery);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fejl: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fejl under afslutning af levering: " + ex.getMessage());
        }
    }




}







