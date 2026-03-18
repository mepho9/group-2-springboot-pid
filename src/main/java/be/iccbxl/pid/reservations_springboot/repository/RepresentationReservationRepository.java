package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.iccbxl.pid.reservations_springboot.model.RepresentationReservation;

public interface RepresentationReservationRepository extends JpaRepository<RepresentationReservation, Long> {
}