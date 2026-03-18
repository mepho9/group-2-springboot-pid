package be.iccbxl.pid.reservations_springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.iccbxl.pid.reservations_springboot.model.Reservation;
import be.iccbxl.pid.reservations_springboot.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
}