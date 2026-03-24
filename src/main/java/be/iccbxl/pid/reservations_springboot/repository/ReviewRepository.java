package be.iccbxl.pid.reservations_springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.iccbxl.pid.reservations_springboot.model.Review;
import be.iccbxl.pid.reservations_springboot.model.Show;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByShow(Show show);
}