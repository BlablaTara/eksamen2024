package com.example.eksamen2024.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long droneId;

    @Column(unique = true, nullable = false)
    private UUID serialUuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneStatus droneStatus;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;

    public Drone() {
    }

    public Drone(UUID serialUuid, DroneStatus droneStatus, Station station, List<Delivery> deliveries) {
        this.serialUuid = serialUuid;
        this.droneStatus = droneStatus;
        this.station = station;
        this.deliveries = deliveries;
    }


    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public UUID getSerialUuid() {
        return serialUuid;
    }

    public void setSerialUuid(UUID serialUuid) {
        this.serialUuid = serialUuid;
    }

    public DroneStatus getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(DroneStatus driftstatus) {
        this.droneStatus = driftstatus;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
