package com.example.eksamen2024.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime actualDeliveryTime;

    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = true)
    @JsonManagedReference
    private Drone drone;

    @OneToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    @JsonManagedReference
    private Pizza pizza;

    public Delivery(Long deliveryId, Drone drone, LocalDateTime expectedDeliveryTime, LocalDateTime actualDeliveryTime) {
    }

    public Delivery() {

    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(LocalDateTime expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }






}
