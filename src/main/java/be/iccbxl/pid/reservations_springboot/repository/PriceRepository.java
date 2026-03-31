package be.iccbxl.pid.reservations_springboot.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.iccbxl.pid.reservations_springboot.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);
    List<Price> findByStartDateLessThanEqualAndEndDateIsNull(LocalDate date);
}