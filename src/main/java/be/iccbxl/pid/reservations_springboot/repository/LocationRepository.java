package be.iccbxl.pid.reservations_springboot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import be.iccbxl.pid.reservations_springboot.model.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
    Location findByDesignation(String designation);
    Optional<Location> findById(Long id);
}
