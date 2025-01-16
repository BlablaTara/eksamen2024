package com.example.eksamen2024.config;

import com.example.eksamen2024.models.Delivery;
import com.example.eksamen2024.models.Drone;
import com.example.eksamen2024.models.DroneStatus;
import com.example.eksamen2024.models.Pizza;
import com.example.eksamen2024.models.Station;
import com.example.eksamen2024.repositories.DeliveryRepository;
import com.example.eksamen2024.repositories.DroneRepository;
import com.example.eksamen2024.repositories.PizzaRepository;
import com.example.eksamen2024.repositories.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class InitData implements CommandLineRunner {

    private final StationRepository stationRepository;
    private final DroneRepository droneRepository;
    private final PizzaRepository pizzaRepository;
    private final DeliveryRepository deliveryRepository;

    public InitData(StationRepository stationRepository, DroneRepository droneRepository,
                    PizzaRepository pizzaRepository, DeliveryRepository deliveryRepository) {
        this.stationRepository = stationRepository;
        this.droneRepository = droneRepository;
        this.pizzaRepository = pizzaRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // STATIONER
        Station station1 = createStation(12.34, 55.41);
        Station station2 = createStation(12.45, 55.67);
        Station station3 = createStation(12.25, 55.50);

        // PIZZA'ER
        Pizza pizza1 = createPizza("Margherita Pizza", 80);
        Pizza pizza2 = createPizza("Pepperoni Pizza", 90);
        Pizza pizza3 = createPizza("Hawaii Pizza", 85);
        Pizza pizza4 = createPizza("Salat Pizza", 85);
        Pizza pizza5 = createPizza("Trés Fromage Pizza", 85);

        // DRONER
        Drone drone1 = createDrone(station1);
        Drone drone2 = createDrone(station2);
        Drone drone3 = createDrone(station3);

        // LEVERINGER
        createDelivery("1234 Pizza Street, Pizza City", drone1, pizza1);
        createDelivery("Kastrupvej, LorteØen", drone1, pizza3);

        createDelivery("Saxogade, FancyStreet", drone2, pizza2);

        createDelivery("Guldberggade, Nørrebronz", drone3, pizza4);
        createDelivery("Blågårds plads, OldHood", drone3, pizza5);
    }

    private Station createStation(double longitude, double latitude) {
        Station station = new Station();
        station.setLongitude(longitude);
        station.setLatitude(latitude);
        return stationRepository.save(station);
    }

    private Pizza createPizza(String titel, int price) {
        Pizza pizza = new Pizza();
        pizza.setTitel(titel);
        pizza.setPrice(price);
        return pizzaRepository.save(pizza);
    }

    private Drone createDrone(Station station) {
        Drone drone = new Drone(
                UUID.randomUUID(),
                DroneStatus.I_DRIFT,
                station,
                null
        );
        return droneRepository.save(drone);
    }

    private void createDelivery(String address, Drone drone, Pizza pizza) {
        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setExpectedDeliveryTime(LocalDateTime.now().plusMinutes(30));
        delivery.setActualDeliveryTime(null);
        delivery.setDrone(drone);
        delivery.setPizza(pizza);

        deliveryRepository.save(delivery);
        System.out.println("Delivery initialized for pizza: " + pizza.getTitel() + " with drone UUID: " + drone.getSerialUuid());
    }
}
