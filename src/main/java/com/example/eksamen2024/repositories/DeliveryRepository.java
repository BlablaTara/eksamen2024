package com.example.eksamen2024.repositories;

import com.example.eksamen2024.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByDroneIsNull();
}
