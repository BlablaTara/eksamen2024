package com.example.eksamen2024.initData;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.Pizza;
import com.example.eksamen2024.repositories.DeliveryRepository;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.PizzaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InitDelivery implements CommandLineRunner {

    private final DeliveryRepository deliveryRepository;
    private final DroneRepository droneRepository;
    private final PizzaRepository pizzaRepository;

    public InitDelivery(DeliveryRepository deliveryRepository, DroneRepository droneRepository, PizzaRepository pizzaRepository) {
        this.deliveryRepository = deliveryRepository;
        this.droneRepository = droneRepository;
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Drone> optionalDrone = droneRepository.findById(1L);
        Optional<Pizza> optionalPizza = pizzaRepository.findById(1L);

        if (optionalDrone.isPresent() && optionalPizza.isPresent()) {
            Drone drone = optionalDrone.get();
            Pizza pizza = optionalPizza.get();

            // Opret og gem delivery
            createDelivery(drone, pizza); // Opret og gem leveringen
        } else {
            System.err.println("Drone or Pizza not found. Cannot initialize delivery.");
        }
    }

    // Hjælpe metode til at oprette delivery
    private void createDelivery(Drone drone, Pizza pizza) {
        Delivery delivery = new Delivery();
        delivery.setAddress("1234 Pizza Street, Pizza City");
        delivery.setExpectedDeliveryTime(LocalDateTime.now().plusMinutes(30)); // Forventet levering om 30 minutter
        delivery.setActualDeliveryTime(null);  // Leveringstidspunkt er endnu ikke registreret
        delivery.setDrone(drone);
        delivery.setPizza(pizza);

        // Gem delivery i databasen
        deliveryRepository.save(delivery);

        System.out.println("Delivery initialized with address: " + delivery.getAddress());
    }
}
