INSERT INTO station (station_id, longitude, latitude) VALUES (1, 12.56, 55.67);
INSERT INTO pizza (pizza_id, titel, price) VALUES
                                               (1, 'Margherita', 70),
                                               (2, 'Pepperoni', 80);
INSERT INTO drone (drone_id, serial_uuid, driftstatus, station_id) VALUES
    (1, '123e4567-e89b-12d3-a456-426614174000', 'I_DRIFT', 1);

INSERT INTO delivery (delivery_id, address, expected_delivery_time, actual_delivery_time, drone_id, pizza_id) VALUES
    (1, 'Testvej 1', '2025-01-16T12:00:00', NULL, 1, 1),
    (2, 'Testvej 2', '2025-01-16T13:00:00', '2025-01-16T13:30:00', 1, 2);