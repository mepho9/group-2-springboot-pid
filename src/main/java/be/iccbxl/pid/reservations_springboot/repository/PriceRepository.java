package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.iccbxl.pid.reservations_springboot.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
}