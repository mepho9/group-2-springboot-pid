package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.iccbxl.pid.reservations_springboot.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}